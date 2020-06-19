package by.vyun.bgb.dto.city;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CityRequestDto {
    Long cityId;
    String logo;
    String name;
}
