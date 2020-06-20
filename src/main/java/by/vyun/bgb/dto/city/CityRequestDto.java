package by.vyun.bgb.dto.city;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class CityRequestDto {
    Long cityId;
    @NotBlank
    String logo;
    @NotBlank
    String name;
}
