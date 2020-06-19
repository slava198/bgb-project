package by.vyun.bgb.converter;



import by.vyun.bgb.dto.city.CityRequestDto;
import by.vyun.bgb.entity.City;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToCityConverter implements Converter<CityRequestDto, City> {
    @Override
    public City convert(CityRequestDto request) {
        return City.builder()
                .name(request.getName())
                .logo(request.getLogo())
                .build();
    }

}
