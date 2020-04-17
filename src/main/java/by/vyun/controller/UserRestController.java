package by.vyun.controller;


import by.vyun.exception.RegistrationException;
import by.vyun.model.BoardGame;
import by.vyun.model.Meeting;
import by.vyun.model.User;
import by.vyun.service.BoardGameService;
import by.vyun.service.MeetingService;
import by.vyun.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/mobile")
public class UserRestController {
    UserService userService;
    BoardGameService gameService;
    MeetingService meetingService;

    @GetMapping("/gameListPage")
    public List<BoardGame> gameListPage(int userId) {
        User currentUser = userService.getUserById(userId);
        return userService.getUnsubscribedGames(currentUser);
    }

    @GetMapping("/allGameList")
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
    public String registration(User user, String cityName) {
        try {
            userService.registration(user, cityName);
        }
        catch (RegistrationException ex) {
            System.out.println(ex.getMessage());
            return ex.getMessage();
        }
        return "ok";
    }

    @GetMapping("/user")
    public User signIn(String login, String password) {
        User signedUser = new User();
        try {
            signedUser = userService.signIn(login, password);
        }
        catch (RegistrationException ex) {
            System.out.println(ex.getMessage());
        }
        return signedUser;
    }

    @PutMapping("/user")
    public User update(@RequestBody int userId, @RequestBody User changedUser) {
        User currentUser = new User();
        try {
            currentUser = userService.update(userId, changedUser);
        }
        catch (RegistrationException ex) {
            System.out.println(ex.getMessage());
        }
        return currentUser;
    }


    //************************************** GAMES CRUD

    @GetMapping("/game")
    public BoardGame seeGame(int gameId) {
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
        //userService.takePartInMeeting(currentUser.getId(), meet.getId());
        return "ok";
    }

    @DeleteMapping("/user/meet")
    public String deleteMeet(int userId, int meetId) {
        userService.deleteMeeting(userId, meetId);
        meetingService.removeMeet(meetId);
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
