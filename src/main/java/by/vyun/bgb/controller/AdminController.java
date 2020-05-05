package by.vyun.bgb.controller;

import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.City;
import by.vyun.bgb.entity.User;
import by.vyun.bgb.exception.RegistrationException;
import by.vyun.bgb.service.BoardGameService;
import by.vyun.bgb.service.CityService;
import by.vyun.bgb.service.UserService;
import by.vyun.bgb.exception.BoardGameException;
import by.vyun.bgb.exception.CityException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    UserService userService;
    BoardGameService gameService;
    CityService cityService;

    private User getCurrentUser() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/createGame_page")
    public String createGame() {
        return "game_create";
    }

    @PostMapping("/add_game")
    public String addGame(BoardGame game, Model model) {
        try {
            gameService.add(game);
        } catch (BoardGameException ex) {
            model.addAttribute("error", ex.getMessage());
            return "game_create";
        }
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("cities", cityService.getAllCities());
        return "admin_page";
    }

    @GetMapping("/add_city")
    public String addCity() {
        return "city_create";
    }

    @PostMapping("/add_city")
    public String addCity(City city, Model model) {
        try {
            cityService.add(city);
        } catch (CityException ex) {
            model.addAttribute("error", ex.getMessage());
            return "city_create";
        }
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("cities", cityService.getAllCities());
        return "admin_page";
    }

    @PostMapping("/update")
    public String update(User changedUser, Model model, MultipartFile imageFile) {
        try {
            User currentUser = getCurrentUser();
            currentUser = userService.update(currentUser.getId(), changedUser, imageFile);
            //session.setAttribute("user", currentUser);
            model.addAttribute("user", currentUser);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("games", gameService.getAllGames());
            model.addAttribute("cities", cityService.getAllCities());
            return "admin_page";
        } catch (RegistrationException | IOException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }


    @GetMapping("/admin_page")
    public String signIn(Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("user", currentUser);
        if (currentUser.getRoles().contains("ROLE_ADMIN")) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("games", gameService.getAllGames());
            model.addAttribute("cities", cityService.getAllCities());
            return "admin_page";
        }
        model.addAttribute("createdMeetings", userService.getCreatedMeets(currentUser));
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "account";
    }

    @GetMapping("/changeGameStatus")
    public String changeGameStatus(int gameId, Model model) {
        gameService.changeGameStatus(gameId);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("cities", cityService.getAllCities());
        return "admin_page";
    }


    @GetMapping("/changeUserStatus")
    public String changeUserStatus(int userId, Model model) {
        userService.changeUserStatus(userId);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("cities", cityService.getAllCities());
        return "admin_page";
    }


}
