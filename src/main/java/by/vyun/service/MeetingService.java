package by.vyun.service;


import by.vyun.model.*;
import by.vyun.repo.CityRepo;
import by.vyun.repo.MeetingRepo;
import by.vyun.repo.MeetingResultRepo;
import by.vyun.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static by.vyun.model.Const.*;

@Service
@AllArgsConstructor
public class MeetingService {
    MeetingRepo meetingRepo;
    MeetingResultRepo meetingResultRepo;
    UserRepo userRepo;
    CityRepo cityRepo;


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

    public Meeting activateMeet(int meetId) {
        Meeting meet = meetingRepo.getFirstById(meetId);
        meet.setState(MeetingState.Activated);
        return meetingRepo.saveAndFlush(meet);
    }

    public void addResults(MeetingResultDTO results, int meetId, User currentUser) {
        Meeting meet = meetingRepo.getFirstById(meetId);
        MeetingResult result;
        float userExperience = currentUser.getExperienceInGame(meet.getGame());
        for (MeetingResultElement resultElement : results.getResults()) {
            result = new MeetingResult();
            result.setFrom(currentUser);
            result.setMeet(meet);
            result.setTo(userRepo.getFirstById(resultElement.getUserIdTo()));
            result.setPoints(((userExperience + ACCELERATING_INDEX) * resultElement.getRate()) / DECELERATING_INDEX);
            meetingResultRepo.save(result);
        }
        System.out.println(getNumberOfNoVoiced(meetId));
    }

    public List<User> getAllVoicedUsers(int meetId) {
        List<User> voicedUsers = new ArrayList<>();
        List<MeetingResult> results = meetingResultRepo.getAllByMeetId(meetId);
            for (MeetingResult meetResult : results) {
                if (!voicedUsers.contains(meetResult.getFrom())) {
                    voicedUsers.add(meetResult.getFrom());
                }
            }
        return voicedUsers;
    }



    public int getNumberOfNoVoiced(int meetId) {
        List<User> members = meetingRepo.getFirstById(meetId).getMembers();
        List<MeetingResult> results = meetingResultRepo.getAllByMeetId(meetId);
        int result = members.size();
        for (User member : members) {
            for (MeetingResult meetResult : results) {
                if (meetResult.getFrom() == member) {
                    result--;
                    break;
                }
            }
        }

        return result;
    }


    public void removeMeet(int id) {
        meetingRepo.deleteById(id);
        meetingRepo.flush();
    }

    public List<City> getAllCities() {
        return cityRepo.findAll();
    }



}
