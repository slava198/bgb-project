package by.vyun.bgb.repository;

import by.vyun.bgb.entity.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardGameRepo extends JpaRepository<BoardGame, Long> {
    Optional<BoardGame> getFirstById(Long gameId);
    //BoardGame getFirstById(Long gameId);
    BoardGame getFirstByTitle(String title);

}
