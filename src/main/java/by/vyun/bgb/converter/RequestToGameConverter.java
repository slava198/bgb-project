package by.vyun.bgb.converter;

import by.vyun.bgb.dto.MeetingDto;
import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.dto.game.GameRequestDto;
import by.vyun.bgb.entity.BoardGame;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RequestToGameConverter implements Converter<GameRequestDto, BoardGame> {

    @Override
    public BoardGame convert(GameRequestDto request) {
        return BoardGame.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .logo(request.getImageUrl())
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
