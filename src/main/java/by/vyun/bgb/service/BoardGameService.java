package by.vyun.bgb.service;

import by.vyun.bgb.exception.InvalidInputException;
import by.vyun.bgb.exception.BoardGameException;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.Rating;
import by.vyun.bgb.repository.BoardGameRepo;
import by.vyun.bgb.repository.RatingRepo;
import by.vyun.bgb.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardGameService {
    private final UserRepo userRepo;
    private final BoardGameRepo gameRepo;
    private final RatingRepo ratingRepo;

    public BoardGameService(UserRepo userRepo, BoardGameRepo gameRepo, RatingRepo ratingRepo) {
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.ratingRepo = ratingRepo;
    }

    public List<BoardGame> getAllGames() {
        return gameRepo.findAll();
    }

    public BoardGame getGameById(int id) {
        return gameRepo.getFirstById(id);
    }

    public BoardGame add(BoardGame game) throws BoardGameException {
        if (game.getTitle().trim().length() * game.getLogo().trim().length() * game.getDescription().trim().length() == 0) {
            throw new BoardGameException("Empty logo, title or description field");
        }
        if (gameRepo.getFirstByTitle(game.getTitle()) != null) {
            throw new BoardGameException("Title duplicated");
        }
        return gameRepo.save(game);
    }

    public BoardGame update(BoardGame currentGame, BoardGame changedGame) throws BoardGameException {
        if (changedGame.getTitle().trim().length() * changedGame.getLogo().trim().length() *
                changedGame.getDescription().trim().length() == 0) {
            throw new BoardGameException("Empty logo, title or description field");
        }
        currentGame.setTitle(changedGame.getTitle());
        currentGame.setLogo(changedGame.getLogo());
        currentGame.setDescription(changedGame.getDescription());
        return gameRepo.saveAndFlush(currentGame);
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
        } else {
            rating = new Rating();
            rating.setGame(gameRepo.getFirstById(gameId));
            rating.setUser(userRepo.getFirstById(userId));
            rating.setGameRate(gameRate);
        }
        ratingRepo.saveAndFlush(rating);
    }

    public double getRatingValueByUserIdAndGameId(int gameId, int userId) {
        Rating rating = ratingRepo.findGameRatingByUserIdAndGameId(userId, gameId);
        if (rating != null) {
            return rating.getGameRate();
        } else {
            return 1;
        }
    }


}
