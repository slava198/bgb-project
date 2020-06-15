package by.vyun.bgb.dto.game;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameDto {
    Long gameId;
    String title;
    String description;
    String imageUrl;
    Float rating;

    private Boolean isActive = true;
    public int numberOfOwners;
    public int numberOfMeetings;

}
