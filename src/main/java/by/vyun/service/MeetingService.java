package by.vyun.service;


import by.vyun.model.City;
import by.vyun.model.Meeting;
import by.vyun.repo.CityRepo;
import by.vyun.repo.MeetingRepo;
import by.vyun.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MeetingService {
    MeetingRepo meetingRepo;
    UserRepo userRepo;
    CityRepo cityRepo;


    public List<Meeting> getAllMeetings() {
        return meetingRepo.findAll();
    }

    public Meeting getMeetingById(int id) {
        return meetingRepo.getFirstById(id);
    }



    public void createMeet(int userId, Meeting meeting, String cityName) {
        meeting.setCity(cityRepo.getFirstByName(cityName));
        meeting.setCreator(userRepo.getFirstById(userId));
        meetingRepo.saveAndFlush(meeting);
    }

    public void removeMeet(int id) {
        meetingRepo.deleteById(id);
        meetingRepo.flush();
    }

    public List<City> getAllCities() {
        return cityRepo.findAll();
    }





}
