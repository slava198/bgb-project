package by.vyun.bgb.service;

import by.vyun.bgb.repository.*;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.exception.UserException;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserRepo userRepo;
    private final BoardGameRepo gameRepo;
    private final MeetingRepo meetingRepo;

    public UserService(UserRepo userRepo, BoardGameRepo gameRepo, MeetingRepo meetingRepo) {
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.meetingRepo = meetingRepo;
    }

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

    public User deleteGame(int userId, long gameId) {
        User user = userRepo.getFirstById(userId);
        user.deleteGameFromCollection(gameRepo.getFirstById(gameId).get());
        return userRepo.saveAndFlush(user);
    }

    public User addGame(int userId, long gameId) {
        User user = userRepo.getFirstById(userId);
        BoardGame game = gameRepo.getFirstById(gameId).get();
        if (!user.getGameCollection().contains(game)) {
            user.addGameToCollection(game);
        }
        gameRepo.flush();
        return userRepo.saveAndFlush(user);
    }

    public List<BoardGame> getUnsubscribedGames(User currentUser) {
        List<BoardGame> allGames = new ArrayList<>();
        for (BoardGame game : gameRepo.findAll()) {
            if (game.getIsActive()) {
                allGames.add(game);
            }
        }
        for (BoardGame game : currentUser.getGameCollection()) {
            if (!game.getIsActive() || allGames.contains(game)) {
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
        return userRepo.saveAndFlush(currentUser);
    }

    public void leaveMeeting(int userId, int meetingId) {
        User currentUser = userRepo.getFirstById(userId);
        currentUser.leaveMeeting(meetingRepo.getFirstById(meetingId));
        userRepo.saveAndFlush(currentUser);
    }

    public void deleteMeeting(int userId, int meetingId) {
        User currentUser = userRepo.getFirstById(userId);
        currentUser.deleteMeeting(meetingRepo.getFirstById(meetingId));
        userRepo.saveAndFlush(currentUser);
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

    public void enable(String login, String code) throws UserException {
        User user = userRepo.getFirstByLogin(login);
        if (user == null) {
            throw new UserException("Invalid login");
        } else if (user.getIsEnabled()) {
            throw new UserException("Account already enabled");
        }
        if (user.getActivationCode().equals(code)) {
            user.setIsEnabled(true);
            userRepo.saveAndFlush(user);
        } else {
            throw new UserException("Invalid activation code");
        }
    }

}
