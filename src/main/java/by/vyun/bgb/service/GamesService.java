package by.vyun.bgb.service;

import by.vyun.bgb.dto.game.CreateGameRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.dto.game.UpdateGameRequestDto;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.repository.BoardGameRepo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GamesService {

    private final BoardGameRepo gameRepo;
    private final Converter<BoardGame, GameDto> converter;

    public GamesService(BoardGameRepo gameRepo,
                        Converter<BoardGame, GameDto> converter) {
        this.gameRepo = gameRepo;
        this.converter = converter;

    }


    public List<GamePreviewDto> getGames() {
        List<GameDto> collect = gameRepo.findAll().stream().map(converter::convert).collect(Collectors.toList());
        return Collections.emptyList();
    }

    public GameDto getGame(Long gameId) {
        return GameDto.builder().build();
    }

    public GameDto createGame(CreateGameRequestDto createGameRequestDto) {
        return GameDto.builder().build();
    }

    public GameDto updateGame(Long gameId, UpdateGameRequestDto updateGameRequestDto) {
        return GameDto.builder().build();
    }

    public void changeGameStatus(Long gameId) {

    }


}
