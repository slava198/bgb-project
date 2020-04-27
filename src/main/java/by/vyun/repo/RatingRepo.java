package by.vyun.repo;

import by.vyun.model.BoardGame;
import by.vyun.model.Rating;
import by.vyun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepo extends JpaRepository<Rating, Integer> {

    Rating findGameRatingByUserIdAndGameId(int userId, int gameId);
    Rating findGameRatingByUserAndGame(User user, BoardGame game);

}
