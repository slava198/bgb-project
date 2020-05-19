package by.vyun.bgb.controller;

import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.City;
import by.vyun.bgb.entity.User;
import by.vyun.bgb.exception.UserException;
import by.vyun.bgb.service.BoardGameService;
import by.vyun.bgb.service.CityService;
import by.vyun.bgb.service.SecurityUserService;
import by.vyun.bgb.service.UserService;
import by.vyun.bgb.exception.BoardGameException;
import by.vyun.bgb.exception.CityException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final BoardGameService gameService;
    private final CityService cityService;
    private final SecurityUserService securityUserService;

    public AdminController(UserService userService, BoardGameService gameService, CityService cityService, SecurityUserService securityUserService) {
        this.userService = userService;
        this.gameService = gameService;
        this.cityService = cityService;
        this.securityUserService = securityUserService;
    }

    private User getCurrentUser() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/add_game")
    public String createGame() {
        return "game_create";
    }

    @PostMapping("/game")
    public String addGame(BoardGame game, Model model) {
        try {
            gameService.add(game);
        } catch (BoardGameException ex) {
            model.addAttribute("error", ex.getMessage());
            return "game_create";
        }
        model.addAttribute("games", gameService.getAllGames());
        return "admin_games";
    }

    @GetMapping("/add_city")
    public String addCity() {
        return "city_create";
    }

    @PostMapping("/city")
    public String addCity(City city, Model model) {
        try {
            cityService.add(city);
        } catch (CityException ex) {
            model.addAttribute("error", ex.getMessage());
            return "city_create";
        }
        model.addAttribute("cities", cityService.getAllCities());
        return "admin_cities";
    }

    @GetMapping("/games")
    public String getGames(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser.getRoles().contains("ROLE_ADMIN")) {
            model.addAttribute("games", gameService.getAllGames());
            return "admin_games";
        }
        model.addAttribute("createdMeetings", userService.getCreatedMeets(currentUser));
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("user", currentUser);
        if (currentUser.getRoles().contains("ROLE_ADMIN")) {
            model.addAttribute("users", userService.getAllUsers());
            return "admin_users";
        }
        model.addAttribute("createdMeetings", userService.getCreatedMeets(currentUser));
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/cities")
    public String getCities(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser.getRoles().contains("ROLE_ADMIN")) {
            model.addAttribute("cities", cityService.getAllCities());
            return "admin_cities";
        }
        model.addAttribute("createdMeetings", userService.getCreatedMeets(currentUser));
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "user_account";
    }

    @GetMapping("/changeGameStatus")
    public String changeGameStatus(int gameId, Model model) {
        gameService.changeGameStatus(gameId);
        model.addAttribute("games", gameService.getAllGames());
        return "admin_games";
    }


    @GetMapping("/changeUserStatus")
    public String changeUserStatus(int userId, Model model) {
        userService.changeUserStatus(userId);
        model.addAttribute("users", userService.getAllUsers());
        return "admin_users";
    }

    @GetMapping("/changeUserPassword")
    public String changeUserPassword(int userId, Model model) {
        try {
            securityUserService.rescuePassword(userId);
        } catch (UserException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            return "admin_users";
        }
        model.addAttribute("users", userService.getAllUsers());
        return "admin_users";
    }


}
