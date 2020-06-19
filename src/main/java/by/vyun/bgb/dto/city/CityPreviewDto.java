package by.vyun.bgb.dto.city;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CityPreviewDto {
    Long cityId;
    String logo;
    String name;
    int numberOfGamers;
    int numberOfMeetings;
}