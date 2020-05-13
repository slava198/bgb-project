package by.vyun.bgb.controller;

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
import org.springframework.web.multipart.MultipartFile;

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


    //************************************** USERS CRUD
    @PostMapping("/user")
    public String registration(User user, String cityName, MultipartFile imageFile) {
        try {
            securityUserService.registration(user, cityName, imageFile);
        } catch (UserException | IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return "ok";
    }

    @GetMapping("/user")
    public User signIn(String login, String password) {
        User signedUser = new User();
        try {
            signedUser = securityUserService.signIn(login, password);
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
        }
        return signedUser;
    }

    @PutMapping("/user")
    public User update(@RequestBody int userId, @RequestBody User changedUser,
                       String newPassword, String newPasswordConfirm,
                       String cityName, MultipartFile imageFile) {
        User currentUser = userService.getUserById(userId);
        try {
            currentUser = securityUserService.update(currentUser, changedUser, newPassword, newPasswordConfirm, cityName, imageFile);
        } catch (UserException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return currentUser;
    }


    //************************************** GAMES CRUD

    @GetMapping("/game/{gameId}")
    public BoardGame seeGame(@PathVariable int gameId) {
        return gameService.getGameById(gameId);
    }

    @PostMapping("/user/game")
    public User addGame(@RequestBody int userId, @RequestBody int gameId) {
        return userService.addGame(userId, gameId);
    }

    @DeleteMapping("/user/game")
    public User deleteGame(@RequestBody int userId, @RequestBody int gameId) {
        return userService.deleteGame(userId, gameId);
    }


    //*********************************** MEETINGS CRUD

    @PostMapping("/user/meet")
    public String createMeet(int userId, int gameId, Meeting meet, String cityName) {
        User currentUser = userService.getUserById(userId);
        meet.setGame(gameService.getGameById(gameId));
        meetingService.createMeet(currentUser.getId(), meet, cityName);
        return "ok";
    }

    @DeleteMapping("/user/meet")
    public String deleteMeet(int userId, int meetId) {
        userService.deleteMeeting(userId, meetId);
        meetingService.deleteMeet(meetId);
        return "ok";
    }

    @PutMapping("/user/meet_in")
    public String addMeeting(int userId, int meetId) {
        userService.takePartInMeeting(userId, meetId);
        return "ok";
    }

    @PutMapping("/user/meet_out")
    public String leaveMeeting(int userId, int meetId) {
        userService.leaveMeeting(userId, meetId);
        return "ok";
    }


}
