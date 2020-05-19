package by.vyun.bgb.controller;

import by.vyun.bgb.entity.*;
import by.vyun.bgb.exception.InvalidInputException;
import by.vyun.bgb.exception.MeetingException;
import by.vyun.bgb.exception.UserException;
import by.vyun.bgb.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final SecurityUserService securityUserService;
    private final BoardGameService gameService;
    private final MeetingService meetingService;
    private final CityService cityService;

    public UserController(UserService userService, SecurityUserService securityUserService,
                          BoardGameService gameService, MeetingService meetingService, CityService cityService) {
        this.userService = userService;
        this.securityUserService = securityUserService;
        this.gameService = gameService;
        this.meetingService = meetingService;
        this.cityService = cityService;
    }

    private User getCurrentUser() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/account")
    public String account(Model model) {
        User signedUser = getCurrentUser();
        model.addAttribute("user", signedUser);
        model.addAttribute("createdMeetings", userService.getCreatedMeets(signedUser));
        model.addAttribute("gameCollection", signedUser.getGameCollection());
        model.addAttribute("meetingSet", signedUser.getMeetingSet());
        model.addAttribute("createdMeets", signedUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("cities", cityService.getAllCityNames());
        return "user_register";
    }

    @GetMapping("/update")
    public String updatePage(Model model) {
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("cities", cityService.getAllCityNames());
        return "user_update";
    }

    @GetMapping("/game_list")
    public String gameListPage(Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("games", userService.getUnsubscribedGames(currentUser));
        return "game_list";
    }

    @GetMapping("/rate_game")
    public String rateGamePage(int gameId, Model model) {
        model.addAttribute("game", gameService.getGameById(gameId));
        model.addAttribute("oldRate",
                gameService.getRatingValueByUserIdAndGameId(gameId, getCurrentUser().getId()));
        return "game_rate";
    }

    @GetMapping("/meet")
    public String seeMeet(Integer meetId, Model model) {
        Meeting meet = meetingService.getMeetingById(meetId);
        model.addAttribute("meet", meet);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("ratingDTO", new MeetingResultDTO());
        model.addAttribute("voicedUsers", meetingService.getVoicedUsers(meetId));
        if (meet.getDateTime().isBefore(LocalDateTime.now()) && meet.getState() == MeetingState.Created) {
            meetingService.startMeet(meetId);
        }
        return "meet_account";
    }

    @GetMapping("/activate_meet")
    public String activateMeet(int meetId, Model model) {
        try {
            meetingService.activateMeet(meetId);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        model.addAttribute("meet", meetingService.getMeetingById(meetId));
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("ratingDTO", new MeetingResultDTO());
        model.addAttribute("voicedUsers", meetingService.getVoicedUsers(meetId));
        return "meet_account";
    }

    @PostMapping("/rate_meeting")
    public String rateMeeting(MeetingResultDTO results, int meetId, Model model) {
        try {
            meetingService.addResults(results, meetId, getCurrentUser());
        } catch (InvalidInputException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        Meeting meet = meetingService.getMeetingById(meetId);
        model.addAttribute("meet", meet);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("ratingDTO", new MeetingResultDTO());
        model.addAttribute("voicedUsers", meetingService.getVoicedUsers(meetId));
        return "meet_account";
    }

    @GetMapping("/close_meet")
    public String closeMeet(int meetId, Model model) {
        try {
            meetingService.closeMeet(meetId);
        } catch (MeetingException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("meet", meetingService.getMeetingById(meetId));
            model.addAttribute("user", getCurrentUser());
            model.addAttribute("ratingDTO", new MeetingResultDTO());
            model.addAttribute("voicedUsers", meetingService.getVoicedUsers(meetId));
            return "meet_account";
        }
        User currentUser = getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/createMeet_page")
    public String createMeet(int gameId, Model model) {
        BoardGame game = gameService.getGameById(gameId);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("game", game);
        model.addAttribute("cities", cityService.getAllCityNames());
        return "meet_create";
    }

    @PostMapping("/login")
    public String login(Model model, User user) {
        try {
            securityUserService.signIn(user.getLogin(), user.getPassword());
        } catch (UserException e) {
            model.addAttribute("error", e.getMessage());
            return "user_login";
        }
        return "redirect:/user/account";
    }

    @PostMapping("/registration")
    public String registration(User user, String passwordConfirm, String cityName,
                               MultipartFile imageFile, Model model) {
        if (user.checkPassword(passwordConfirm)) {
            model.addAttribute("error", "Password and it's confirmations are the different!");
            model.addAttribute("cities", cityService.getAllCityNames());
            return "user_register";
        }
        try {
            securityUserService.registration(user, cityName, imageFile);
        } catch (UserException | IOException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cities", cityService.getAllCityNames());
            return "user_register";
        }
        return "redirect:/";
    }

    @GetMapping("/enable")
    public String enablePage() {
        return "user_enable";
    }

    @PostMapping("/enable")
    public String enable(String login, String code, Model model) {
        try {
            userService.enable(login, code);
        } catch (UserException e) {
            model.addAttribute("error", e.getMessage());
            return "user_enable";
        }
        model.addAttribute("enabled", "true");
        return "user_login";
    }

    @PostMapping("/update")
    public String update(User changedUser,
                         String newPassword, String newPasswordConfirm,
                         String cityName, MultipartFile imageFile, Model model) {
        User currentUser = getCurrentUser();
        try {
            securityUserService.update(currentUser, changedUser, newPassword, newPasswordConfirm, cityName, imageFile);
        } catch (UserException | IOException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("user", getCurrentUser());
            model.addAttribute("cities", cityService.getAllCityNames());
            return "user_update";
        }
        return "redirect:/user/account";
    }

    @GetMapping("/add_game")
    public String addGame(Integer gameId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.addGame(currentUser.getId(), gameId);
        model.addAttribute("user", currentUser);
        model.addAttribute("games", userService.getUnsubscribedGames(getCurrentUser()));
        return "game_list";
    }

    @GetMapping("/remove_game")
    public String removeGame(Integer gameId) {
        User currentUser = userService.deleteGame(getCurrentUser().getId(), gameId);
        userService.getUserById(currentUser.getId());
        return "redirect:/user/account";
    }

    @PostMapping("/rate_game")
    public String rateGame(Integer gameId, float rate, Model model) {
        User currentUser = getCurrentUser();
        BoardGame game = gameService.getGameById(gameId);
        try {
            gameService.rateGame(gameId, currentUser.getId(), rate);
        } catch (InvalidInputException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("game", game);
            model.addAttribute("oldRate",
                    gameService.getRatingValueByUserIdAndGameId(gameId, getCurrentUser().getId()));
            return "game_rate";
        }
        model.addAttribute("meetings", game.getMeetings());
        model.addAttribute("game", game);
        model.addAttribute("user", currentUser);
        return "game_account";
    }

    @GetMapping("/game")
    public String seeGame(Integer gameId, Model model) {
        BoardGame game = gameService.getGameById(gameId);
        model.addAttribute("game", game);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("meetings", game.getMeetings());
        return "game_account";
    }

    @GetMapping("/delete_meet")
    public String deleteMeet(int meetId) {
        User currentUser = getCurrentUser();
        userService.deleteMeeting(currentUser.getId(), meetId);
        meetingService.deleteMeet(meetId);
        return "redirect:/user/account";
    }

    @PostMapping("/create_meet")
    public String createMeet(String cityName, String location,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                             int gameId, Model model) {
        User currentUser = getCurrentUser();
        BoardGame game = gameService.getGameById(gameId);
        Meeting meet = new Meeting();
        meet.setLocation(location);
        meet.setDateTime(dateTime);
        meet.setGame(game);
        meet = meetingService.createMeet(currentUser.getId(), meet, cityName);
        userService.takePartInMeeting(currentUser.getId(), meet.getId());
        currentUser = userService.getUserById(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("meetings", game.getMeetings());
        model.addAttribute("game", game);
        return "game_account";
    }

    @GetMapping("/meet_in")
    public String addMeeting(int meetId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.takePartInMeeting(currentUser.getId(), meetId);
        BoardGame game = meetingService.getMeetingById(meetId).getGame();
        model.addAttribute("game", game);
        model.addAttribute("user", currentUser);
        model.addAttribute("meetings", game.getMeetings());
        return "game_account";
    }

    @GetMapping("/meet_out")
    public String leaveMeeting(int meetId) {
        User currentUser = getCurrentUser();
        userService.leaveMeeting(currentUser.getId(), meetId);
        return "redirect:/user/account";
    }

    @GetMapping("/delete_from_meet")
    public String deleteUserFromMeeting(int userId, int meetId) {
        userService.leaveMeeting(userId, meetId);
        return "redirect:/user/meet?meetId=" + meetId;
    }

    @GetMapping("/back")
    public String back() {
        return "redirect:/user/account";
    }

}
