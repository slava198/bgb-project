package by.vyun.service;


import by.vyun.exception.BoardGameException;
import by.vyun.exception.InvalidInputException;
import by.vyun.model.BoardGame;
import by.vyun.model.Rating;
import by.vyun.repo.BoardGameRepo;
import by.vyun.repo.RatingRepo;
import by.vyun.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardGameService {
    UserRepo userRepo;
    BoardGameRepo gameRepo;
    RatingRepo ratingRepo;

    public List<BoardGame> getAllGames() {
        return gameRepo.findAll();
    }

    public BoardGame getGameById(int id) {
        return gameRepo.getFirstById(id);
    }


    public BoardGame add(BoardGame game) throws BoardGameException {
        if (game.getTitle().trim().length() * game.getLogo().trim().length() * game.getDescription().trim().length() == 0) {
            throw new BoardGameException("Empty logo, title or description field!");
        }
        if (gameRepo.getFirstByTitle(game.getTitle()) != null) {
            throw new BoardGameException("Title duplicated!");
        }
        return gameRepo.save(game);
    }

    public void changeGameStatus(int id) {
        BoardGame game = gameRepo.getOne(id);
        game.setIsActive(!game.getIsActive());
        gameRepo.saveAndFlush(game);

    }


    public void rateGame(Integer gameId, Integer userId, float gameRate) throws InvalidInputException {
        if (gameRate < 1 || gameRate > 10) {
            throw new InvalidInputException("Rating must be from 1 to 10");
        }
        Rating rating = ratingRepo.findGameRatingByUserIdAndGameId(userId, gameId);
        if (rating != null) {
            rating.setGameRate(gameRate);
        }
        else {
            rating = new Rating();
            rating.setGame(gameRepo.getFirstById(gameId));
            rating.setUser(userRepo.getFirstById(userId));
            rating.setGameRate(gameRate);
        }
        ratingRepo.saveAndFlush(rating);
    }

    public float getGameRatingByUserIdAndGameId(int gameId, int userId) {
        Rating rating = ratingRepo.findGameRatingByUserIdAndGameId(userId, gameId);
        if (rating != null) {
            return rating.getGameRate();
        }
        else {
            return 1;
        }

    }


}
