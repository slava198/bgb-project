package by.vyun.bgb.service;

import by.vyun.bgb.entity.*;
import by.vyun.bgb.exception.InvalidInputException;
import by.vyun.bgb.exception.MeetingException;
import by.vyun.bgb.exception.UserException;
import by.vyun.bgb.repository.*;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static by.vyun.bgb.entity.Const.*;

@Service
public class MeetingService {
    private final MeetingRepo meetingRepo;
    private final MeetingResultRepo meetingResultRepo;
    private final UserRepo userRepo;
    private final CityRepo cityRepo;
    private final RatingRepo ratingRepo;
    private final Validator validator;

    public MeetingService(MeetingRepo meetingRepo, MeetingResultRepo meetingResultRepo,
                          UserRepo userRepo, CityRepo cityRepo, RatingRepo ratingRepo) {
        this.meetingRepo = meetingRepo;
        this.meetingResultRepo = meetingResultRepo;
        this.userRepo = userRepo;
        this.cityRepo = cityRepo;
        this.ratingRepo = ratingRepo;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepo.findAll();
    }


    public Meeting getMeetingById(int id) {
        return meetingRepo.getFirstById(id);
    }

    public Meeting createMeet(int userId, Meeting meeting, String cityName) throws MeetingException {
        Set<ConstraintViolation<Meeting>> violations = validator.validate(meeting);
        if (!violations.isEmpty()) {
            throw new MeetingException(violations.iterator().next().getMessage());
        }
        meeting.setCity(cityRepo.getFirstByName(cityName));
        meeting.setCreator(userRepo.getFirstById(userId));
        return meetingRepo.saveAndFlush(meeting);
    }

    public void startMeet(int meetId) {
        Meeting meet = meetingRepo.getFirstById(meetId);
        meet.setState(MeetingState.Started);
        meetingRepo.saveAndFlush(meet);
    }

    public void activateMeet(int meetId) throws MeetingException {
        Meeting meet = meetingRepo.getFirstById(meetId);
        if (meet.getNumberOfMembers() < 2) {
            throw new MeetingException("Meeting should have at least 2 members");
        }
        meet.setState(MeetingState.Activated);
        meetingRepo.saveAndFlush(meet);
    }

    public void addResults(MeetingResultDTO results, int meetId, User currentUser) throws InvalidInputException {
        int ratingsSum = 0;
        for (MeetingResultElement resultElement : results.getResults()) {
            if (resultElement.getRate() == null) {
                throw new InvalidInputException("All rating fields should be filled");
            }
            ratingsSum += resultElement.getRate();
        }
        Meeting meet = meetingRepo.getFirstById(meetId);
        if (ratingsSum > meet.getNumberOfMembers()) {
            throw new InvalidInputException("Sum of the rates shouldn't exceed number of the members");
        }
        MeetingResult result;
        double userExperience = currentUser.getExperienceInGame(meet.getGame());
        for (MeetingResultElement resultElement : results.getResults()) {
            result = new MeetingResult();
            result.setFrom(currentUser);
            result.setMeet(meet);
            result.setTo(userRepo.getFirstById(resultElement.getUserIdTo()));
            result.setPoints(((userExperience + ACCELERATE_INDEX) * resultElement.getRate()) / DECELERATE_INDEX);
            meetingResultRepo.save(result);
        }
    }

    public void closeMeet(int meetId) throws MeetingException {
        if (getNumberOfVoiced(meetId) < 2) {
            throw new MeetingException("At least 2 members should voiced");
        }
        BoardGame game = meetingRepo.getFirstById(meetId).getGame();
        List<User> voicedMembers = getVoicedUsers(meetId);
        List<MeetingResult> meetingResults = meetingResultRepo.getAllByMeetId(meetId);
        for (User member : voicedMembers) {
            for (MeetingResult result : meetingResults) {
                if (result.getTo() == member) {
                    member.addExperienceInGame(game, result.getPoints());
                }
            }
            Rating rating = member.getRatingInGame(game);
            rating.setCompletedMeets(rating.getCompletedMeets() + 1);
            ratingRepo.saveAndFlush(rating);
        }
        deleteMeet(meetId);
    }

    public List<User> getVoicedUsers(int meetId) {
        List<User> voicedUsers = new ArrayList<>();
        List<MeetingResult> results = meetingResultRepo.getAllByMeetId(meetId);
        for (MeetingResult meetResult : results) {
            if (!voicedUsers.contains(meetResult.getFrom())) {
                voicedUsers.add(meetResult.getFrom());
            }
        }
        return voicedUsers;
    }

    private int getNumberOfVoiced(int meetId) {
        List<User> members = meetingRepo.getFirstById(meetId).getMembers();
        List<MeetingResult> results = meetingResultRepo.getAllByMeetId(meetId);
        int result = 0;
        for (User member : members) {
            for (MeetingResult meetResult : results) {
                if (meetResult.getFrom() == member) {
                    result++;
                    break;
                }
            }
        }
        return result;
    }

    public void deleteMeet(int id) {
        meetingRepo.deleteById(id);
        meetingRepo.flush();
    }


}
