package by.vyun.bgb.controller.api;

import by.vyun.bgb.dto.RatingRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.service.UserGamesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/{userId}/games")
public class UserGamesController {

    private final UserGamesService userGamesService;

    public UserGamesController(UserGamesService userGamesService) {
        this.userGamesService = userGamesService;
    }

    @GetMapping
    public ResponseEntity<List<GamePreviewDto>> getUserGames(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userGamesService.getUserGames(userId));
    }

    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable("userId") Long userId, @PathVariable("gameId") Long gameId) {
        userGamesService.deleteUserGame(userId, gameId);
    }

    @PutMapping("/{gameId}/rating/{gameRating}")
    public void rateGame(@PathVariable("userId") Long userId,
                         @PathVariable("gameId") Long gameId,
                         @Valid @PathVariable("gameRating") int gameRating) {
        userGamesService.rateGame(userId, gameId, gameRating);
    }

    @PostMapping("/{gameId}")
    public ResponseEntity<GameDto> addGame(@PathVariable("userId") Long userId, @PathVariable("gameId") Long gameId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userGamesService.addUserGame(userId, gameId));
    }

}

