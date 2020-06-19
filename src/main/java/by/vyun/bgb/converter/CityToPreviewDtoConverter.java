package by.vyun.bgb.converter;

import by.vyun.bgb.dto.city.CityPreviewDto;
import by.vyun.bgb.entity.City;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CityToPreviewDtoConverter implements Converter<City, CityPreviewDto> {
    @Override
    public CityPreviewDto convert(City city) {
        return CityPreviewDto.builder()
                .cityId(city.getId())
                .name(city.getName())
                .logo(city.getLogo())
                .numberOfMeetings(city.getNumberOfMeetings())
                .numberOfGamers(city.getNumberOfGamers())
                .build();
    }
}

