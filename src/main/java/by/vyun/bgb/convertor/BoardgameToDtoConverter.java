package by.vyun.bgb.convertor;

import by.vyun.bgb.dto.BoardgameDto;
import by.vyun.bgb.dto.MeetingDto;
import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.User;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

public class BoardgameToDtoConverter implements Converter<BoardGame, BoardgameDto> {


    @Override
    public BoardgameDto convert(BoardGame game) {
        return BoardgameDto.builder()
                .id(game.getId())
                .title(game.getTitle())
                .logo(game.getLogo())
                .description(game.getDescription())
                .numberOfOwners(game.getNumberOfOwners())
                .numberOfMeetings(game.getNumberOfMeetings())
                .ratingValue(game.getRatingValue())
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
