package by.vyun.bgb.controller.api;

import by.vyun.bgb.dto.game.GameRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.dto.game.UpdateGameRequestDto;

import by.vyun.bgb.service.GamesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("games")
public class GamesController {

    private final GamesService gamesService;

    public GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    //todo extend with paging ans soring
    //todo extend with filter
    @GetMapping
    //return only active games
    public ResponseEntity<List<GamePreviewDto>> getGames() {
        return ResponseEntity.ok(gamesService.getGames());
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDto> getGame(@PathVariable("gameId") Long gameId) {
            return ResponseEntity.ok(gamesService.getGame(gameId));
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame(@Valid @RequestBody GameRequestDto createGameRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gamesService.createGame(createGameRequest));
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<GameDto> updateGame(@PathVariable("gameId") Long gameId,
                                              @Valid @RequestBody GameRequestDto updateGameRequest) {
        return ResponseEntity.ok(gamesService.updateGame(gameId, updateGameRequest));
    }

    //todo security endpoint for admin
    @PutMapping("/{gameId}/status")
    public void changeStatus(@PathVariable("gameId") Long gameId) {
        gamesService.changeGameStatus(gameId);
    }

}

