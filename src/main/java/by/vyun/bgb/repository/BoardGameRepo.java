package by.vyun.bgb.repository;

import by.vyun.bgb.entity.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameRepo extends JpaRepository<BoardGame, Integer> {
    BoardGame getFirstById(int gameId);
    BoardGame getFirstByTitle(String title);

}
