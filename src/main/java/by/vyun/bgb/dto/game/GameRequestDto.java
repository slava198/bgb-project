package by.vyun.bgb.dto.game;


import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class GameRequestDto {
    String title;
    String description;
    String imageUrl;
}
