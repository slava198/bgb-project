package by.vyun.bgb.converter;


import by.vyun.bgb.dto.MeetingDto;
import by.vyun.bgb.dto.RatingDto;
import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.Rating;
import by.vyun.bgb.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserToDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .isEnabled(user.getIsEnabled())
                .age(user.getAge())
                .dateOfBirth(user.getDateOfBirth().toString())
                .avatar(user.getAvatar())
                .city(user.getCity().getName())
                .address(user.getAddress())
                .meetings(getMeetings(user.getMeetingSet()))
                .games(getGames(user.getGameCollection()))
                .ratings(getRatings(user.getRatings()))
                .totalExperience(user.getTotalExperience())
                .completedMeetsNumber(user.getCompletedMeetsNumber())
                .build();
    }

    private List<GameDto> getGames(List<BoardGame> games) {
        GameToDtoConverter converter = new GameToDtoConverter();
        List<GameDto> gamesDto = new ArrayList<>();
        for (BoardGame game : games) {
            gamesDto.add(converter.convert(game));
        }
        return gamesDto;
    }

    private List<MeetingDto> getMeetings(List<Meeting> meetings) {
        MeetingToDtoConverter converter = new MeetingToDtoConverter();
        List<MeetingDto> meetingsDto = new ArrayList<>();
        for (Meeting meeting : meetings) {
            meetingsDto.add(converter.convert(meeting));
        }
        return meetingsDto;
    }


    private List<RatingDto> getRatings(List<Rating> ratings) {
        RatingToDtoConverter converter = new RatingToDtoConverter();
        List<RatingDto> ratingsDto = new ArrayList<>();
        for (Rating rating : ratings) {
            ratingsDto.add(converter.convert(rating));
        }
        return ratingsDto;
    }

}
