package by.vyun.bgb.service;

import by.vyun.bgb.controller.api.UpdateGameRequestDto;
import by.vyun.bgb.dto.game.CreateGameRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GamesService {

    public List<GameDto> getGames() {
        return Collections.emptyList();
    }

    public GameDto getGame(Long gameId) {
        return new GameDto();
    }

    public GameDto createGame(CreateGameRequestDto createGameRequestDto) {
        return new GameDto();
    }

    public GameDto updateGame(Long gameId, UpdateGameRequestDto updateGameRequestDto) {
        return new GameDto();
    }

    public void changeGameStatus(Long gameId) {

    }


}
