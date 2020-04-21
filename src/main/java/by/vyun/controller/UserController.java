package by.vyun.controller;

import by.vyun.exception.RegistrationException;
import by.vyun.model.*;
import by.vyun.service.BoardGameService;
import by.vyun.service.CityService;
import by.vyun.service.MeetingService;
import by.vyun.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    UserService userService;
    BoardGameService gameService;
    MeetingService meetingService;
    CityService cityService;

    private User getCurrentUser() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("cities", cityService.getAllCityNames());
        return "registration";
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
        model.addAttribute("gameId", gameId);
        return "game_rate";
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
//    @GetMapping("/login")
//    public String loginPage(Model model, String error, String logout) {
//        if (error != null)
//            model.addAttribute("error", "Your username and password is invalid.");
//        if (logout != null)
//            model.addAttribute("message", "You have been logged out successfully.");
//        return "login";
//    }


    @PostMapping("/registration")
    public String registration(User user, String passwordConfirm, String cityName, Model model) {
        if (user.checkPassword(passwordConfirm)) {
            model.addAttribute("error", "Password and it's confirmations are the different!");
            model.addAttribute("cities", cityService.getAllCityNames());
            return "registration";
        }
        try {
            userService.registration(user, cityName);
        } catch (RegistrationException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("cities", cityService.getAllCityNames());
            return "registration";
        }
        return "redirect:/";

    }


    @PostMapping("/update")
    public String update(User changedUser, String newPassword, String newPasswordConfirm, String cityName, Model model) {
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
            currentUser = userService.update(currentUser.getId(), changedUser);
            currentUser = userService.getUserById(currentUser.getId());
            model.addAttribute("user", currentUser);
            model.addAttribute("gameCollection", currentUser.getGameCollection());
            model.addAttribute("meetingSet", currentUser.getMeetingSet());
            model.addAttribute("createdMeets", currentUser.getCreatedMeets());
            return "account";
        } catch (RegistrationException ex) {
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
        return "account";
    }

    @GetMapping("/rate_game")
    public String rateGame(Integer gameId, float rate, Model model) {
        User currentUser = getCurrentUser();
        gameService.rateGame(gameId, currentUser.getId(), rate);
        model.addAttribute("game", gameService.getGameById(gameId));
        model.addAttribute("user", currentUser);
        return "game_account";
    }


    @GetMapping("/see_game")
    public String seeGame(Integer gameId, Model model) {
        BoardGame game = gameService.getGameById(gameId);
        model.addAttribute("game", game);
        model.addAttribute("user", getCurrentUser());
        return "game_account";
    }

//***********************************end game


    //********************************begin meet
    @GetMapping("/delete_meet")
    public String deleteMeet(int meetId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.deleteMeeting(currentUser.getId(), meetId);
        meetingService.removeMeet(meetId);
        currentUser = userService.getUserById(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "account";
    }

    @PostMapping("/create_meet")
    public String createMeet(String cityName, String location,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                             int gameId, Model model) {
        User currentUser = getCurrentUser();
        Meeting meet = new Meeting();
        meet.setLocation(location);
        meet.setDateTime(dateTime);
        meet.setGame(gameService.getGameById(gameId));
        meetingService.createMeet(currentUser.getId(), meet, cityName);
        currentUser = userService.getUserById(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("game", gameService.getGameById(gameId));
        return "game_account";
    }

    @GetMapping("/add_meet")
    public String addMeeting(int meetId, Model model) {
        User currentUser = getCurrentUser();
        currentUser = userService.takePartInMeeting(currentUser.getId(), meetId);
        model.addAttribute("game", meetingService.getMeetingById(meetId).getGame());
        model.addAttribute("user", currentUser);
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
        return "account";
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
        return "account";
    }


}
