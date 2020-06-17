package by.vyun.bgb.service;

import by.vyun.bgb.converter.GameToDtoConverter;
import by.vyun.bgb.converter.GameToPreviewDtoConverter;
import by.vyun.bgb.converter.RequestToGameConverter;
import by.vyun.bgb.dto.RatingRequestDto;
import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.User;
import by.vyun.bgb.exception.ResourceNotFoundException;
import by.vyun.bgb.repository.BoardGameRepo;
import by.vyun.bgb.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGamesService {
    private final BoardGameRepo gameRepo;
    private final UserRepo userRepo;
    private final GameToPreviewDtoConverter gameToPreviewConverter;
    private final GameToDtoConverter gameToDtoConverter;
    private final RequestToGameConverter requestToGameConverter;

    public UserGamesService(BoardGameRepo gameRepo, UserRepo userRepo,
                            GameToPreviewDtoConverter gameToPreviewConverter,
                            GameToDtoConverter gameToDtoConverter,
                            RequestToGameConverter requestToGameConverter) {
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
        this.gameToPreviewConverter = gameToPreviewConverter;
        this.gameToDtoConverter = gameToDtoConverter;
        this.requestToGameConverter = requestToGameConverter;
    }


    public List<GamePreviewDto> getUserGames(Long userId) {
        final User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return gameRepo.findAll()
                .stream()
                .filter(game -> userEntity.getGameCollection().contains(game))
                .filter(BoardGame::getIsActive)
                .map(gameToPreviewConverter::convert)
                .collect(Collectors.toList());
    }

    public void deleteUserGame(Long userId, Long gameId) {

    }

    public void rateGame(Long userId, Long gameId, RatingRequestDto ratingRequestDto) {

    }

    public GameDto addGame(Long userId, Long gameId) {
        return GameDto.builder().build();
    }

}

