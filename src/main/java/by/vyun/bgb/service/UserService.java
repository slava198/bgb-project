package by.vyun.bgb.service;

import by.vyun.bgb.entity.Image;
import by.vyun.bgb.repository.*;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.exception.RegistrationException;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static by.vyun.bgb.entity.Const.DEFAULT_AVATAR;


@Service
@AllArgsConstructor
public class UserService {
    UserRepo userRepo;
    BoardGameRepo gameRepo;
    MeetingRepo meetingRepo;
    CityRepo cityRepo;
    ImageRepo imageRepo;


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


    public User registration(User user, String cityName, MultipartFile imageFile) throws RegistrationException, IOException {
        if (user.getLogin().trim().length() * user.getPassword().trim().length() * cityName.trim().length() == 0) {
            throw new RegistrationException("Empty login, password or location field!");
        }
        if (userRepo.getFirstByLogin(user.getLogin()) != null) {
            throw new RegistrationException("Login duplicated!");
        }
        Image avatar = new Image();
        if (imageFile == null) {
            avatar.setData(imageRepo.findFirstByName(DEFAULT_AVATAR).getData());
            avatar.setName(user.getLogin());
        } else {
            avatar.setData(imageFile.getBytes());
            avatar.setName(imageFile.getOriginalFilename());
        }
        imageRepo.saveAndFlush(avatar);
        user.setAvatar(avatar);
        user.setCity(cityRepo.getFirstByName(cityName));
        user = userRepo.save(user);
        return user;
    }


    public User signIn(String login, String password) throws RegistrationException {
        User foundedUser = userRepo.getFirstByLogin(login);
        if (foundedUser == null) {
            throw new RegistrationException("Login not founded!");
        }
        if (foundedUser.checkPassword(password)) {
            throw new RegistrationException("Invalid password!");
        }
        return foundedUser;
    }


    public User update(int id, User changedUser, MultipartFile imageFile) throws RegistrationException, IOException {
        User currentUser = userRepo.getFirstById(id);
        if (!imageFile.isEmpty()) {
            Image avatar = imageRepo.findFirstById(currentUser.getAvatar().getId());
            avatar.setData(imageFile.getBytes());
            avatar.setName(imageFile.getOriginalFilename());
            imageRepo.saveAndFlush(avatar);
            currentUser.setAvatar(avatar);
        }
        currentUser.setCity(changedUser.getCity());
        currentUser.setAddress(changedUser.getAddress());
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
