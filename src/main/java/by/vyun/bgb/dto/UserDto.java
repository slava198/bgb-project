package by.vyun.bgb.dto;


import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;
import by.vyun.bgb.entity.City;
import by.vyun.bgb.entity.Image;
import by.vyun.bgb.entity.Meeting;
import by.vyun.bgb.entity.Rating;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;


@Value
@Builder
@JsonIgnoreProperties
public class UserDto {
    private long id;
    private String login;
    private int age;
    private String dateOfBirth;
    private String address;
    private String email;
    private Boolean isActive;
    private Boolean isEnabled;
    private String avatar;
//    private Image avatar;
    private String city;
    //private List<RatingDto> ratings;
    private List<GameDto> games;
    private List<MeetingDto> meetings;
    private double totalExperience;
    private int completedMeetsNumber;





}
