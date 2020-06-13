package by.vyun.bgb.service;

import by.vyun.bgb.convertor.GameToDtoConverter;
import by.vyun.bgb.dto.game.UpdateGameRequestDto;
import by.vyun.bgb.dto.game.CreateGameRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.repository.BoardGameRepo;
import by.vyun.bgb.repository.RatingRepo;
import by.vyun.bgb.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GamesService {
    private final UserRepo userRepo;
    private final BoardGameRepo gameRepo;
    private final GameToDtoConverter converter;
    private final RatingRepo ratingRepo;

    public GamesService(UserRepo userRepo, BoardGameRepo gameRepo,
                        RatingRepo ratingRepo, GameToDtoConverter converter) {
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.ratingRepo = ratingRepo;
        this.converter = converter;

    }


    public List<GameDto> getGames() {
        List<GameDto> bgDtoList =  gameRepo.findAll().stream().map(converter::convert).collect(Collectors.toList());
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
