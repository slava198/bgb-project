package by.vyun.bgb.repository;

import by.vyun.bgb.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
    Rating findGameRatingByUserIdAndGameId(int userId, long gameId);

}
