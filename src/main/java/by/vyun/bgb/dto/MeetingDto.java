package by.vyun.bgb.dto;

import by.vyun.bgb.entity.City;
import by.vyun.bgb.entity.MeetingState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@JsonIgnoreProperties
public class MeetingDto {

    private int id;
    private String city;
    private String location;
    private LocalDateTime dateTime;
    private String creator;
    private String game;
    //private List<UserDto> members;
    private MeetingState state;
    private int numberOfMembers;



}
