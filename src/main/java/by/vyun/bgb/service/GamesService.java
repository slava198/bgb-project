package by.vyun.bgb.service;

import by.vyun.bgb.converter.GameToPreviewDtoConverter;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.dto.game.UpdateGameRequestDto;
import by.vyun.bgb.dto.game.GameRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.entity.BoardGame;

import by.vyun.bgb.exception.ResourceDuplicateException;
import by.vyun.bgb.exception.ResourceNotFoundException;

import by.vyun.bgb.repository.BoardGameRepo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GamesService {
    private final BoardGameRepo gameRepo;
    private final GameToPreviewDtoConverter converter;

    public GamesService(BoardGameRepo gameRepo, GameToPreviewDtoConverter converter) {
        this.gameRepo = gameRepo;
        this.converter = converter;
    }


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


    public GameDto createGame(GameRequestDto gameRequestDto) {
       if(gameRepo.getFirstByTitle(gameRequestDto.getTitle()) != null) {
           throw new ResourceDuplicateException("Game duplicated: " +  gameRequestDto.getTitle());
       }
       BoardGame game = gameRepo.save(BoardGame.builder()
                .title(gameRequestDto.getTitle())
                .logo(gameRequestDto.getImageUrl())
                .description(gameRequestDto.getDescription())
                .build());
        return GameDto.builder()
                .gameId(game.getId())
                .title(game.getTitle())
                .imageUrl(game.getLogo())
                .description(game.getDescription())
                .rating(game.getRatingValue())
                .numberOfOwners(game.getNumberOfOwners())
                .numberOfMeetings(game.getNumberOfMeetings())
                .build();
    }


    public GameDto updateGame(Long gameId, GameRequestDto gameRequestDto) {
        final BoardGame game = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));
        game.setTitle(gameRequestDto.getTitle());
        game.setLogo(gameRequestDto.getImageUrl());
        game.setDescription(gameRequestDto.getDescription());
        gameRepo.save(game);

        return GameDto.builder()
                .gameId(game.getId())
                .title(game.getTitle())
                .imageUrl(game.getLogo())
                .description(game.getDescription())
                .rating(game.getRatingValue())
                .numberOfOwners(game.getNumberOfOwners())
                .numberOfMeetings(game.getNumberOfMeetings())
                .build();
    }

    public void changeGameStatus(Long gameId) {
        BoardGame game = gameRepo.getOne(gameId);
        game.setIsActive(!game.getIsActive());
        gameRepo.save(game);

    }



}
