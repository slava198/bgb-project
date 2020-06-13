package by.vyun.bgb.dto.game;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameDto {
    Long gameId;
    String title;
    String description;
    String image_url;
    Double rating;
    Integer numberOfOwners;
    Integer numberOfMeetings;
}
