package by.vyun.bgb.controller;

import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.exception.MeetingException;
import by.vyun.bgb.exception.UserException;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.User;
import by.vyun.bgb.service.BoardGameService;
import by.vyun.bgb.service.MeetingService;
import by.vyun.bgb.service.SecurityUserService;
import by.vyun.bgb.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/mobile")
public class UserRestController {
    private final UserService userService;
    private final SecurityUserService securityUserService;
    private final BoardGameService gameService;
    private final MeetingService meetingService;

    public UserRestController(UserService userService, SecurityUserService securityUserService, BoardGameService gameService, MeetingService meetingService) {
        this.userService = userService;
        this.securityUserService = securityUserService;
        this.gameService = gameService;
        this.meetingService = meetingService;
    }

    @GetMapping("/gameListPage")
    public List<BoardGame> gameListPage(int userId) {
        User currentUser = userService.getUserById(userId);
        return userService.getUnsubscribedGames(currentUser);
    }

    @GetMapping("/gameList")
    @ResponseBody
    public List<BoardGame> allGameList() {
        return gameService.getAllGames();
    }

    @PostMapping("/userGameList")
    public List<BoardGame> gameList(int userId) {
        User currentUser = userService.getUserById(userId);
        return currentUser.getGameCollection();
    }

    @GetMapping("/userMeetingList")
    public List<Meeting> meetingList(int userId) {
        User currentUser = userService.getUserById(userId);
        return currentUser.getMeetingSet();
    }

    @PostMapping("/user")
    public String registration(@RequestBody User user, String passwordConfirm,
                               String cityName) {

        try {
            securityUserService.registration(user, passwordConfirm, cityName);
        } catch (UserException | IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return "true";
    }

    @PostMapping("/enable")
    public String enable(String login, String code) {
        try {
            userService.enable(login, code);
        } catch (UserException e) {
            return  e.getMessage();
        }
        return "true";
    }

    @GetMapping("/user")
    public UserDto signIn(String login, String password) {
        UserDto signedUser = null;
        try {
            signedUser = securityUserService.signIn(login, password);
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
        }
        return signedUser;
    }

    @PutMapping("/user")
    public User update(int userId, @RequestBody User changedUser,
                       String newPassword, String newPasswordConfirm,
                       String cityName) {
        User currentUser = userService.getUserById(userId);
        try {
            currentUser = securityUserService.update(currentUser, changedUser, newPassword, newPasswordConfirm, cityName);
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
        }
        return currentUser;
    }

    @GetMapping("/game/{gameId}")
    public BoardGame seeGame(@PathVariable int gameId) {
        return gameService.getGameById(gameId);
    }

    @PostMapping("/user/game")
    public User addGame(int userId, int gameId) {
        return userService.addGame(userId, gameId);
    }

    @DeleteMapping("/user/game")
    public User deleteGame(int userId, int gameId) {
        return userService.deleteGame(userId, gameId);
    }

    @PostMapping("/user/meet")
    public String createMeet(int userId, int gameId, @RequestBody Meeting meet, String cityName) {
        //User currentUser = userService.getUserById(userId);
        meet.setGame(gameService.getGameById(gameId));
        try {
            meetingService.createMeet(userId, meet, cityName);
        } catch (MeetingException e) {
            System.out.println(e.getMessage());
            return "false";
        }
        return "true";
    }

    @DeleteMapping("/user/meet")
    public String deleteMeet(int userId, int meetId) {
        userService.deleteMeeting(userId, meetId);
        meetingService.deleteMeet(meetId);
        return "true";
    }

    @PutMapping("/user/meet_in")
    public String addMeeting(int userId, int meetId) {
        userService.takePartInMeeting(userId, meetId);
        return "true";
    }

    @PutMapping("/user/meet_out")
    public String leaveMeeting(int userId, int meetId) {
        userService.leaveMeeting(userId, meetId);
        return "true";
    }

}
