package by.vyun.bgb.convertor;

import by.vyun.bgb.dto.MeetingDto;
import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.User;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeetingToDtoConverter implements Converter<Meeting, MeetingDto> {
    @Override
    public MeetingDto convert(Meeting meeting) {
        return MeetingDto.builder()
                .id(meeting.getId())
                .city(meeting.getCity().getName())
                .location(meeting.getLocation())
                .dateTime(meeting.getDateTime())
                .creator(meeting.getCreator().getLogin())
                .game(meeting.getGame().getTitle())
                .numberOfMembers(meeting.getNumberOfMembers())
                //.members(getMembers(meeting.getMembers()))
                .state(meeting.getState())
                .build();
    }

    private List<UserDto> getMembers(List<User> users) {
        UserToDtoConverter converter = new UserToDtoConverter();
        List<UserDto> members = new ArrayList<>();
        for (User user : users) {
            members.add(converter.convert(user));
        }
        return members;
    }


}
