package by.vyun.bgb.dto.game;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GamePreviewDto {

    Long gameId;
    String title;
    String imageUrl;
    Double rating;
}
