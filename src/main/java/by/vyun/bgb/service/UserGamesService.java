package by.vyun.bgb.service;

import by.vyun.bgb.dto.RatingRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserGamesService {

    public List<GameDto> getUserGames(Long userId) {
        return Collections.emptyList();
    }

    public void deleteUserGame(Long userId, Long gameId) {

    }

    public void rateGame(Long userId, Long gameId, RatingRequestDto ratingRequestDto) {

    }

    public GameDto addGame(Long userId, Long gameId) {
        return GameDto.builder().build();
    }
}