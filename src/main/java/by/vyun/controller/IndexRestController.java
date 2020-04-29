package by.vyun.controller;

import by.vyun.model.BoardGame;
import by.vyun.service.BoardGameService;
import by.vyun.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/mobile")
public class IndexRestController {
    UserService userService;
    BoardGameService gameService;

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
