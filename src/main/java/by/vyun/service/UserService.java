package by.vyun.service;

import by.vyun.model.BoardGame;
import by.vyun.exception.RegistrationException;
import by.vyun.model.Meeting;
import by.vyun.model.User;
import by.vyun.repo.BoardGameRepo;
import by.vyun.repo.CityRepo;
import by.vyun.repo.MeetingRepo;
import by.vyun.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class UserService {

    UserRepo userRepo;
    BoardGameRepo gameRepo;
    MeetingRepo meetingRepo;
    CityRepo cityRepo;

    public void changeUserStatus(int userId) {
        User user = userRepo.getFirstById(userId);
        user.setIsActive(!user.getIsActive());
        userRepo.saveAndFlush(user);
    }

    public User getUserById(Integer id) {
        return userRepo.getFirstById(id);
    }
    public User getUserByLogin(String login) {
        return userRepo.getFirstByLogin(login);
    }


    public User registration(User user, String cityName) throws RegistrationException {

        if (user.getLogin().trim().length() * user.getPassword().trim().length() * cityName.trim().length() == 0) {
            throw new RegistrationException("Empty login, password or location field!");
        }
        if (userRepo.getFirstByLogin(user.getLogin()) != null) {
            throw new RegistrationException("Login duplicated!");
        }
        user.setCity(cityRepo.getFirstByName(cityName));
        user = userRepo.save(user);
        return user;
    }


    public User signIn(String login, String password) throws RegistrationException {
        User foundedUser = userRepo.getFirstByLogin(login);
        if (foundedUser == null) {
            throw new RegistrationException("Login not founded!");
        }
        if (!foundedUser.checkPassword(password)) {
            throw new RegistrationException("Invalid password!");
        }
        return foundedUser;
    }


    public User update(int id, User changedUser) throws RegistrationException {
            User currentUser = userRepo.getFirstById(id);
            currentUser.setCity(changedUser.getCity());
            currentUser.setDateOfBirth(changedUser.getDateOfBirth());
            currentUser.setPassword(changedUser.getPassword());
            return userRepo.saveAndFlush(currentUser);
    }


    public User deleteGame(int userId, int gameId) {
        User user = userRepo.getFirstById(userId);
        user.deleteGameFromCollection(gameRepo.getFirstById(gameId));
        return userRepo.saveAndFlush(user);
    }


    public User addGame(int userId, int gameId) {
        User user = userRepo.getFirstById(userId);
        BoardGame game = gameRepo.getFirstById(gameId);
        if (!user.getGameCollection().contains(game)) {
            user.addGameToCollection(game);
        }
        gameRepo.flush();
        return userRepo.saveAndFlush(user);
    }


    public List<BoardGame> getUnsubscribedGames(User currentUser) {
//        List<BoardGame> unsubscribedGames = new ArrayList<>();
        List<BoardGame> allGames = gameRepo.findAll();
        for (BoardGame game : currentUser.getGameCollection()) {
            if (allGames.contains(game)) {
                allGames.remove(game);
            }
        }
        return allGames;
    }



    public User takePartInMeeting(int userId, int meetingId) {
        User currentUser = userRepo.getFirstById(userId);
        Meeting meeting = meetingRepo.getFirstById(meetingId);
        if (!currentUser.getMeetingSet().contains(meeting)) {
            currentUser.addMeeting(meeting);
        }
        //meetingRepo.flush();
        return userRepo.saveAndFlush(currentUser);
    }

    public User leaveMeeting(int userId, int meetingId) {
        User currentUser = userRepo.getFirstById(userId);
        currentUser.leaveMeeting(meetingRepo.getFirstById(meetingId));
        return userRepo.saveAndFlush(currentUser);
    }

    public User deleteMeeting(int userId, int meetingId) {
        User currentUser = userRepo.getFirstById(userId);
        currentUser.deleteMeeting(meetingRepo.getFirstById(meetingId));
        return userRepo.saveAndFlush(currentUser);
    }

    public List<Meeting> getCreatedMeets(User currentUser) {
        List<Meeting> createdMeets = new ArrayList<>();
        for (Meeting meet : currentUser.getMeetingSet()) {
            if (meet.getCreator().equals(currentUser)) {
                createdMeets.add(meet);
            }
        }
        return createdMeets;
    }


    public List<User> getAllUsers() {
        return userRepo.findAll();
    }



}
