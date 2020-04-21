package by.vyun.repo;

import by.vyun.model.BoardGame;
import by.vyun.model.GameRating;
import by.vyun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRatingRepo extends JpaRepository<GameRating, Integer> {

    GameRating findGameRatingByUserIdAndGameId(int userId, int gameId);
    GameRating findGameRatingByUserAndGame(User user, BoardGame game);

}
