package by.vyun.controller;

import by.vyun.model.User;
import by.vyun.service.BoardGameService;
import by.vyun.service.MeetingService;
import by.vyun.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller
//@NoArgsConstructor
@AllArgsConstructor
public class IndexController {

    UserService userService;
    BoardGameService gameService;
    MeetingService meetingService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("error", session.getAttribute("error"));
        session.removeAttribute("error");
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("meetings", meetingService.getAllMeetings());
        return "index";
    }

    @PostMapping("/account")
    public String account(Model model) {
        User signedUser = userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("user", signedUser);
        model.addAttribute("createdMeetings", userService.getCreatedMeets(signedUser));
        model.addAttribute("gameCollection", signedUser.getGameCollection());
        model.addAttribute("meetingSet", signedUser.getMeetingSet());
        model.addAttribute("createdMeets", signedUser.getCreatedMeets());

        return "account";
    }
}
