package by.vyun.bgb.controller;

import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.service.BoardGameService;
import by.vyun.bgb.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mobile")
public class IndexRestController {
    private final UserService userService;
    private final BoardGameService gameService;

    public IndexRestController(UserService userService, BoardGameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping
    public List<Map<String, String>> index() {
        List<Map<String, String>> games = new ArrayList<>();
        for (BoardGame game : gameService.getAllGames()) {
            games.add(new HashMap<String, String>() {{
                put("id", game.getId().toString());
                put("title", game.getTitle());
                put("logo", game.getLogo());
            }});
        }
        return games;
    }

}
