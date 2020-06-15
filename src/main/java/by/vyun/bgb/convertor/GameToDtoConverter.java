package by.vyun.bgb.convertor;


import by.vyun.bgb.dto.MeetingDto;
import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GameToDtoConverter implements Converter<BoardGame, GameDto> {


    @Override
    public GameDto convert(BoardGame game) {
        return GameDto.builder()
                .gameId(game.getId().longValue())
                .title(game.getTitle())
                .image_url(game.getLogo())
                .description(game.getDescription())
                .numberOfOwners(game.getNumberOfOwners())
                .numberOfMeetings(game.getNumberOfMeetings())
                .rating(game.getRatingValue())
                //.meetings(getMeetings(game.getMeetings()))
                //.owners(getOwners(game.getOwners()))
                .build();
    }

    private Set<UserDto> getOwners(Set<User> users) {
        UserToDtoConverter converter = new UserToDtoConverter();
        Set<UserDto> owners = new HashSet<>();
        for (User user : users) {
            owners.add(converter.convert(user));
        }
        return owners;
    }

    private Set<MeetingDto> getMeetings(Set<Meeting> meetings) {
        MeetingToDtoConverter converter = new MeetingToDtoConverter();
        Set<MeetingDto> meetingsDto = new HashSet<>();
        for (Meeting meeting : meetings) {
            meetingsDto.add(converter.convert(meeting));
        }
        return meetingsDto;
    }



}
