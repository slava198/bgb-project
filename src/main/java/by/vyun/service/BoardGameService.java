package by.vyun.service;


import by.vyun.exception.BoardGameException;
import by.vyun.model.BoardGame;
import by.vyun.model.GameRating;
import by.vyun.repo.BoardGameRepo;
import by.vyun.repo.GameRatingRepo;
import by.vyun.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardGameService {
    UserRepo userRepo;
    BoardGameRepo gameRepo;
    GameRatingRepo gameRatingRepo;

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


    public void rateGame(Integer gameId, Integer userId, float rate) {
        GameRating rating = gameRatingRepo.findGameRatingByUserIdAndGameId(userId, gameId);
        if (rating != null) {
            rating.setRate(rate);
            gameRatingRepo.saveAndFlush(rating);
        }
        else {
            gameRatingRepo.saveAndFlush(new GameRating(gameRepo.getFirstById(gameId),
                    userRepo.getFirstById(userId), rate));
        }
    }
}
