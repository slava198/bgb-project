package by.vyun.bgb.repository;

import by.vyun.bgb.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepo extends JpaRepository<Rating, Long> {
    Rating findGameRatingByUserIdAndGameId(Long userId, Long gameId);

}
