package by.vyun.bgb.controller;

import by.vyun.bgb.entity.*;
import by.vyun.bgb.exception.InvalidInputException;
import by.vyun.bgb.exception.MeetingException;
import by.vyun.bgb.exception.RegistrationException;
import by.vyun.bgb.repository.ImageRepo;
import by.vyun.bgb.service.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final BoardGameService gameService;
    private final MeetingService meetingService;
    private final CityService cityService;
    private final ImageRepo imageRepo;



    private User getCurrentUser() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }





    @PostMapping("/image")
    public String saveImg(String name, MultipartFile file, Model model) throws IOException {
        Image image = new Image();
        image.setName(name);
        byte[] byteImg = file.getBytes();
        image.setData(byteImg);
        imageRepo.save(image);
        User currentUser = getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/image")
    public String showImg(String name, Model model) {
        Image image = imageRepo.findFirstByName(name);
//        String encodedImg = Base64.getEncoder().encodeToString(image.getData());
        model.addAttribute("img", image);
        return "image";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("cities", cityService.getAllCityNames());
        return "user_register";
    }

    @GetMapping("/update_page")
    public String updatePage(Model model) {
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("cities", cityService.getAllCityNames());
        return "user_update";
    }

    @GetMapping("/gameList_page")
    public String gameListPage(Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("games", userService.getUnsubscribedGames(currentUser));
        return "game_list";
    }

    @GetMapping("/rateGame_page")
    public String rateGamePage(int gameId, Model model) {
        model.addAttribute("game", gameService.getGameById(gameId));
        model.addAttribute("oldRate",
                gameService.getRatingValueByUserIdAndGameId(gameId, getCurrentUser().getId()));
        return "game_rate";
    }

    @GetMapping("/see_meet")
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
        try{
            meetingService.activateMeet(meetId);
        }
        catch (Exception ex) {
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


    //**********************begin user

    @PostMapping("/login")
    public String login(Model model, User user) {
        User currentUser;
        try {
            currentUser = userService.signIn(user.getLogin(), user.getPassword());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
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
            userService.registration(user, cityName, imageFile);
        } catch (RegistrationException | IOException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cities", cityService.getAllCityNames());
            return "registration";
        }
        return "redirect:/";
    }


    @PostMapping("/update")
    public String update(User changedUser, String newPassword, String newPasswordConfirm, String cityName,
                         MultipartFile imageFile, Model model) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser.checkPassword(changedUser.getPassword())) {
                model.addAttribute("error", "Invalid current password!");
                model.addAttribute("user", currentUser);
                return "user_update";
            }
            if (!newPassword.equals(newPasswordConfirm)) {
                model.addAttribute("error", "New password and it's confirmations are the different!");
                model.addAttribute("user", currentUser);
                return "user_update";
            }
            changedUser.setPassword(newPassword);
            changedUser.setCity(cityService.getCityByName(cityName));
            currentUser = userService.update(currentUser.getId(), changedUser, imageFile);
            currentUser = userService.getUserById(currentUser.getId());
            model.addAttribute("user", currentUser);
            model.addAttribute("gameCollection", currentUser.getGameCollection());
            model.addAttribute("meetingSet", currentUser.getMeetingSet());
            model.addAttribute("createdMeets", currentUser.getCreatedMeets());
            return "user_account";
        } catch (RegistrationException | IOException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "redirect:/";

    }
//*******************************end user


    //*****************begin game
    @GetMapping("/add_game")
    public String addGame(Integer gameId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.addGame(currentUser.getId(), gameId);
        model.addAttribute("user", currentUser);
        model.addAttribute("games", userService.getUnsubscribedGames(getCurrentUser()));
        return "game_list";
    }

    @GetMapping("/remove_game")
    public String removeGame(Integer gameId, Model model) {
        User currentUser = userService.deleteGame(getCurrentUser().getId(), gameId);
        currentUser = userService.getUserById(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/rate_game")
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

    @GetMapping("/see_game")
    public String seeGame(Integer gameId, Model model) {
        BoardGame game = gameService.getGameById(gameId);
        model.addAttribute("game", game);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("meetings", game.getMeetings());
        return "game_account";
    }
//***********************************end game


    //********************************begin meet
    @GetMapping("/delete_meet")
    public String deleteMeet(int meetId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.deleteMeeting(currentUser.getId(), meetId);
        meetingService.deleteMeet(meetId);
        currentUser = userService.getUserById(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
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

    @GetMapping("/add_meet")
    public String addMeeting(int meetId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.takePartInMeeting(currentUser.getId(), meetId);
        BoardGame game = meetingService.getMeetingById(meetId).getGame();
        model.addAttribute("game", game);
        model.addAttribute("user", currentUser);
        model.addAttribute("meetings", game.getMeetings());
        return "game_account";
    }

    @GetMapping("/leave_meet")
    public String leaveMeeting(int meetId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.leaveMeeting(currentUser.getId(), meetId);
        currentUser = userService.getUserById(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/delete_user_from_meet")
    public String deleteUserFromMeeting(int userId, int meetId, Model model) {
        userService.leaveMeeting(userId, meetId);
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
    //**********************************end meet


    @GetMapping("/back")
    public String back(Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.getUserById(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }


}
