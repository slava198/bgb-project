package by.vyun.bgb.service;

import by.vyun.bgb.entity.*;
import by.vyun.bgb.exception.InvalidInputException;
import by.vyun.bgb.exception.MeetingException;
import by.vyun.bgb.repository.*;
import by.vyun.bgb.entity.*;
import by.vyun.bgb.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static by.vyun.bgb.entity.Const.*;

@Service
@AllArgsConstructor
public class MeetingService {
    MeetingRepo meetingRepo;
    MeetingResultRepo meetingResultRepo;
    UserRepo userRepo;
    CityRepo cityRepo;
    BoardGameRepo boardGameRepo;
    RatingRepo ratingRepo;


    public List<Meeting> getAllMeetings() {
        return meetingRepo.findAll();
    }


    public Meeting getMeetingById(int id) {
        return meetingRepo.getFirstById(id);
    }


    public Meeting createMeet(int userId, Meeting meeting, String cityName) {
        meeting.setCity(cityRepo.getFirstByName(cityName));
        meeting.setCreator(userRepo.getFirstById(userId));
        return meetingRepo.saveAndFlush(meeting);
    }


    public Meeting startMeet(int meetId) {
        Meeting meet = meetingRepo.getFirstById(meetId);
        meet.setState(MeetingState.Started);
        return meetingRepo.saveAndFlush(meet);
    }


    public Meeting activateMeet(int meetId) throws MeetingException {
        Meeting meet = meetingRepo.getFirstById(meetId);
        if (meet.getNumberOfMembers() < 2) {
            throw new MeetingException("Meeting should have at least 2 members");
        }
        meet.setState(MeetingState.Activated);
        return meetingRepo.saveAndFlush(meet);
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


    public int getNumberOfVoiced(int meetId) {
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


    public List<City> getAllCities() {
        return cityRepo.findAll();
    }


}
