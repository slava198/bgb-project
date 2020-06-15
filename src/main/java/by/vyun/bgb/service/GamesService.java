package by.vyun.bgb.service;

import by.vyun.bgb.converter.GameToPreviewDtoConverter;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.dto.game.UpdateGameRequestDto;
import by.vyun.bgb.dto.game.CreateGameRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.entity.BoardGame;

import by.vyun.bgb.exception.ResourceNotFoundException;

import by.vyun.bgb.repository.BoardGameRepo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GamesService {
    //private final UserRepo userRepo;
    private final BoardGameRepo gameRepo;
    private final GameToPreviewDtoConverter converter;
    //private final RatingRepo ratingRepo;

    public GamesService(BoardGameRepo gameRepo, GameToPreviewDtoConverter converter) {
        //this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        //this.ratingRepo = ratingRepo;
        this.converter = converter;

    }


//    public List<GameDto> getGames() {
//        List<GameDto> bgDtoList = gameRepo.findAll().stream()
//                .filter(BoardGame::getIsActive)
//                .map(converter::convert)
//                .collect(Collectors.toList());
//        return bgDtoList;
//
//
//    }
//
//    public GameDto getGame(Long gameId) throws BoardGameException {
//        Optional<BoardGame> game = gameRepo.getFirstById(gameId);
//        if (game.isPresent()) {
//            return converter.convert(game.get());
//        } else {
//            throw new BoardGameException("Not found");
//        }
//        return GameDto.builder().build();


    public List<GamePreviewDto> getGames() {
        return gameRepo.findAll()
                .stream()
                .filter(BoardGame::getIsActive)
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    public GameDto getGame(Long gameId) {
        final BoardGame gameEntity = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));
        return GameDto.builder()
                .gameId(gameEntity.getId())
                .title(gameEntity.getTitle())
                .imageUrl(gameEntity.getLogo())
                .rating(gameEntity.getRatingValue())
                .numberOfMeetings(gameEntity.getNumberOfMeetings())
                .numberOfOwners(gameEntity.getNumberOfOwners())
                .build();
    }

    public GameDto createGame(CreateGameRequestDto createGameRequestDto) {
        return GameDto.builder().build();
    }

    public GameDto updateGame(Long gameId, UpdateGameRequestDto updateGameRequestDto) {
        return GameDto.builder().build();
    }

    public void changeGameStatus(Long gameId) {

    }


}
