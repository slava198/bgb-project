package by.vyun.bgb.service;

import by.vyun.bgb.converter.GameToDtoConverter;
import by.vyun.bgb.converter.GameToPreviewDtoConverter;
import by.vyun.bgb.converter.RequestToGameConverter;
import by.vyun.bgb.dto.game.GamePreviewDto;
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
    private final GameToPreviewDtoConverter gameToPreviewConverter;
    private final GameToDtoConverter gameToDtoConverter;
    private final RequestToGameConverter requestToGameConverter;

    public GamesService(BoardGameRepo gameRepo,
                        GameToPreviewDtoConverter gameToPreviewConverter,
                        GameToDtoConverter gameToDtoConverter,
                        RequestToGameConverter requestToGameConverter) {
        this.gameRepo = gameRepo;
        this.gameToPreviewConverter = gameToPreviewConverter;
        this.gameToDtoConverter = gameToDtoConverter;
        this.requestToGameConverter = requestToGameConverter;
    }

    public List<GamePreviewDto> getGames() {
        return gameRepo.findAll()
                .stream()
                .filter(BoardGame::getIsActive)
                .map(gameToPreviewConverter::convert)
                .collect(Collectors.toList());
    }

    public GameDto getGame(Long gameId) {
        final BoardGame gameEntity = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));
        return gameToDtoConverter.convert(gameEntity);
    }

    public GameDto createGame(GameRequestDto gameRequestDto) {
        if (gameRepo.getFirstByTitle(gameRequestDto.getTitle()) != null) {
            throw new ResourceDuplicateException("Game", gameRequestDto.getTitle());
        }
        return gameToDtoConverter.convert(gameRepo.save(requestToGameConverter.convert(gameRequestDto)));
    }

    public GameDto updateGame(Long gameId, GameRequestDto gameRequestDto) {
        if (gameRepo.getFirstByTitle(gameRequestDto.getTitle()) != null) {
            throw new ResourceDuplicateException("Game", gameRequestDto.getTitle());
        }
        final BoardGame gameEntity = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));

        gameEntity.setTitle(gameRequestDto.getTitle());
        gameEntity.setLogo(gameRequestDto.getImageUrl());
        gameEntity.setDescription(gameRequestDto.getDescription());
        return gameToDtoConverter.convert(gameRepo.save(gameEntity));
    }

    public void changeGameStatus(Long gameId) {
        gameRepo.save(gameRepo.getOne(gameId).inverseActive());
    }

}
