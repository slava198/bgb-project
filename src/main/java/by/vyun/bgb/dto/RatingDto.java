package by.vyun.bgb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonIgnoreProperties
public class RatingDto {

    private int id;
    private String game;
    private String user;
    private double gameRate;
    private double userExperience;
    private int completedMeets;


}
