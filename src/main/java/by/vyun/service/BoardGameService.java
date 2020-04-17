package by.vyun.service;


import by.vyun.exception.BoardGameException;
import by.vyun.exception.RegistrationException;
import by.vyun.model.BoardGame;
import by.vyun.repo.BoardGameRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BoardGameService {
    BoardGameRepo gameRepo;

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



}
