package by.vyun.repo;

import by.vyun.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
    Rating findGameRatingByUserIdAndGameId(int userId, int gameId);

}
