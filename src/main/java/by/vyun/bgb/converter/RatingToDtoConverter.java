package by.vyun.bgb.converter;

import by.vyun.bgb.dto.RatingDto;
import by.vyun.bgb.entity.Rating;
import org.springframework.core.convert.converter.Converter;

public class RatingToDtoConverter implements Converter<Rating, RatingDto> {
    @Override
    public RatingDto convert(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .game(rating.getGame().getTitle())
                .user(rating.getUser().getLogin())
                .gameRate(rating.getGameRate())
                .userExperience(rating.getUserExperience())
                .completedMeets(rating.getCompletedMeets())
                .build();
    }
}
