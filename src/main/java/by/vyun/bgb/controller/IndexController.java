package by.vyun.bgb.controller;

import by.vyun.bgb.service.BoardGameService;
import by.vyun.bgb.service.UserService;
import by.vyun.bgb.entity.User;
import by.vyun.bgb.service.MeetingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class IndexController {
    private final UserService userService;
    private final BoardGameService gameService;
    private final MeetingService meetingService;

    public IndexController(UserService userService, BoardGameService gameService, MeetingService meetingService) {
        this.userService = userService;
        this.gameService = gameService;
        this.meetingService = meetingService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("meetings", meetingService.getAllMeetings());
        return "index";
    }

    @GetMapping("login")
    public String login() {
        return "user_login";
    }


//    @PostMapping("/account")
//    public String accountPost(Model model) {
//        User signedUser = userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
//        model.addAttribute("user", signedUser);
//        model.addAttribute("createdMeetings", userService.getCreatedMeets(signedUser));
//        model.addAttribute("gameCollection", signedUser.getGameCollection());
//        model.addAttribute("meetingSet", signedUser.getMeetingSet());
//        model.addAttribute("createdMeets", signedUser.getCreatedMeets());
//        return "user_account";
//    }


}
