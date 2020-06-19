package by.vyun.bgb.service;

import by.vyun.bgb.converter.GameToDtoConverter;
import by.vyun.bgb.converter.GameToPreviewDtoConverter;
import by.vyun.bgb.converter.RequestToGameConverter;
import by.vyun.bgb.dto.RatingRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.Rating;
import by.vyun.bgb.entity.User;
import by.vyun.bgb.exception.ResourceDuplicateException;
import by.vyun.bgb.exception.ResourceNotFoundException;
import by.vyun.bgb.repository.BoardGameRepo;
import by.vyun.bgb.repository.RatingRepo;
import by.vyun.bgb.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGamesService {
    private final BoardGameRepo gameRepo;
    private final UserRepo userRepo;
    private final GameToPreviewDtoConverter gameToPreviewConverter;
    private final RatingRepo ratingRepo;


    public UserGamesService(BoardGameRepo gameRepo, UserRepo userRepo, RatingRepo ratingRepo,
                            GameToPreviewDtoConverter gameToPreviewConverter) {
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
        this.ratingRepo = ratingRepo;
        this.gameToPreviewConverter = gameToPreviewConverter;
    }


    public List<GamePreviewDto> getUserGames(Long userId) {
        final User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        List<GamePreviewDto> lst = gameRepo.findAll()
                .stream()
                .filter(game -> userEntity.getGameCollection().contains(game))
                .filter(BoardGame::getIsActive)
                .map(gameToPreviewConverter::convert)
                .collect(Collectors.toList());
        return lst;
    }

    public void deleteUserGame(Long userId, Long gameId) {
        final User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        final BoardGame gameEntity = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));
        userEntity.deleteGame(gameEntity);
        userRepo.save(userEntity);
    }

    public void rateGame(Long userId, Long gameId, int gameRate) {
        final User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        final BoardGame gameEntity = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));
        // todo: check game in collection
        Rating rating = ratingRepo.findGameRatingByUserIdAndGameId(userId, gameId);
        if (rating != null) {
            rating.setGameRate(gameRate);
        } else {
            rating = new Rating();
            rating.setUser(userEntity);
            rating.setGame(gameEntity);
            rating.setGameRate(gameRate);
        }
        ratingRepo.save(rating);

    }

    public GameDto addUserGame(Long userId, Long gameId) {
        final User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        final BoardGame gameEntity = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));
        if (userEntity.getGameCollection().contains(gameEntity)) {
            throw new ResourceDuplicateException("User games", gameEntity.getTitle());
        }
        userEntity.addGame(gameEntity);
        userRepo.save(userEntity);
        gameRepo.flush();
        return GameDto.builder().build();
    }

}

