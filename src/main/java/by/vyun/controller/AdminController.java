package by.vyun.controller;

import by.vyun.exception.BoardGameException;
import by.vyun.exception.CityException;
import by.vyun.model.BoardGame;
import by.vyun.exception.RegistrationException;
import by.vyun.model.City;
import by.vyun.model.User;
import by.vyun.service.BoardGameService;
import by.vyun.service.CityService;
import by.vyun.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    UserService userService;
    BoardGameService gameService;
    CityService cityService;

    User getCurrentUser() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

//    @GetMapping("registrationPage")
//    public String registrationPage() {
//        return "registration";
//
//    }

    @GetMapping("/createGame_page")
    public String createGame() {
        return "game_create";

    }

    @PostMapping("/add_game")
    public String addGame(BoardGame game, Model model, HttpSession session) {
        try {
            gameService.add(game);
        }
        catch (BoardGameException ex) {
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
        }
        catch (CityException ex) {
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
    public String update(User changedUser, Model model, HttpSession session) {
        try {
            User currentUser = getCurrentUser();
            currentUser = userService.update(currentUser.getId(), changedUser);
            //session.setAttribute("user", currentUser);
            model.addAttribute("user", currentUser);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("games", gameService.getAllGames());
            model.addAttribute("cities", cityService.getAllCities());
            return "admin_page";
        }
        catch (RegistrationException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "redirect:/";

    }


    @GetMapping("/admin_page")
    public String signIn(HttpSession session, Model model) {
        User currentUser = getCurrentUser();
        if (currentUser.getRoles().contains("ROLE_ADMIN")) {
            model.addAttribute("user", getCurrentUser());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("games", gameService.getAllGames());
            model.addAttribute("cities", cityService.getAllCities());
            return "admin_page";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("createdMeetings", userService.getCreatedMeets(currentUser));
        model.addAttribute("gameCollection", currentUser.getGameCollection());
        model.addAttribute("meetingSet", currentUser.getMeetingSet());
        model.addAttribute("createdMeets", currentUser.getCreatedMeets());
        return "account";

    }

//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.removeAttribute("user");
//        return "redirect:/";
//    }

    @GetMapping("/changeGameStatus")
        public String changeGameStatus(int gameId, HttpSession session, Model model) {
            gameService.changeGameStatus(gameId);
            //session.setAttribute("games", gameService.getAllGames());
            model.addAttribute("user", getCurrentUser());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("games", gameService.getAllGames());
            model.addAttribute("cities", cityService.getAllCities());
            return "admin_page";
    }


    @GetMapping("/changeUserStatus")
    public String changeUserStatus(int userId, HttpSession session, Model model) {
        userService.changeUserStatus(userId);
        //session.setAttribute("games", gameService.getAllGames());
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("cities", cityService.getAllCities());
        return "admin_page";
    }








}
