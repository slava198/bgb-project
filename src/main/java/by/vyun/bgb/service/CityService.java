package by.vyun.bgb.service;

import by.vyun.bgb.converter.CityToPreviewDtoConverter;
import by.vyun.bgb.converter.RequestToCityConverter;
import by.vyun.bgb.dto.city.CityPreviewDto;
import by.vyun.bgb.dto.city.CityRequestDto;
import by.vyun.bgb.entity.City;
import by.vyun.bgb.exception.CityException;
import by.vyun.bgb.exception.ResourceDuplicateException;
import by.vyun.bgb.exception.ResourceNotFoundException;
import by.vyun.bgb.repository.CityRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CityService {
    private final CityRepo cityRepo;
    private final CityToPreviewDtoConverter cityToPreviewConverter;
    private final RequestToCityConverter requestToCityConverter;


    public CityPreviewDto createCity(CityRequestDto cityRequestDto) {
        if (cityRepo.getFirstByName(cityRequestDto.getName()) != null) {
            throw new ResourceDuplicateException("City", cityRequestDto.getName());
        }
        return cityToPreviewConverter.convert(cityRepo.save(requestToCityConverter.convert(cityRequestDto)));


    }

    public City add(City city) throws CityException {
        if (city.getName().trim().length() * city.getLogo().trim().length() == 0) {
            throw new CityException("Empty name or logo field");
        }
        if (cityRepo.getFirstByName(city.getName()) != null) {
            throw new CityException("City name duplicated");
        }
        return cityRepo.save(city);
    }

    public List<CityPreviewDto> getCities() {
        List<CityPreviewDto> lst = cityRepo.findAll()
                .stream()
                .map(cityToPreviewConverter::convert)
                .collect(Collectors.toList());
        return lst;
    }

    public List<String> getAllCityNames() {
        List<String> cityNames = getCities()
                .stream()
                .map(CityPreviewDto::getName)
                .collect(Collectors.toList());
        return cityNames;
    }

    public City getCityByName(String cityName) {
        return cityRepo.getFirstByName(cityName);
    }


    public CityPreviewDto getCity(Long cityId) {
        final City cityEntity = cityRepo.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City", cityId));
        return cityToPreviewConverter.convert(cityEntity);
    }


    public CityPreviewDto updateCity(Long cityId, CityRequestDto updateCityRequest) {
        final City cityEntity = cityRepo.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City", cityId));
        if (cityRepo.getFirstByName(updateCityRequest.getName()) != null) {
            throw new ResourceDuplicateException("City", updateCityRequest.getName());
        }

        cityEntity.setName(updateCityRequest.getName());
        cityEntity.setLogo(updateCityRequest.getLogo());
        return cityToPreviewConverter.convert(cityRepo.save(cityEntity));


    }
}
