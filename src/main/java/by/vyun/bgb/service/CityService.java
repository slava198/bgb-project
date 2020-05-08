package by.vyun.bgb.service;

import by.vyun.bgb.exception.CityException;
import by.vyun.bgb.entity.City;
import by.vyun.bgb.repository.CityRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CityService {
    CityRepo cityRepo;

    public City add(City city) throws CityException {
        if (city.getName().trim().length() * city.getLogo().trim().length() == 0) {
            throw new CityException("Empty name or logo field");
        }
        if (cityRepo.getFirstByName(city.getName()) != null) {
            throw new CityException("City name duplicated");
        }
        return cityRepo.save(city);
    }

    public List<City> getAllCities() {
        return cityRepo.findAll();
    }

    public List<String> getAllCityNames() {
        List<String> cityNames = new ArrayList<>();
        for (City city : getAllCities()) {
            cityNames.add(city.getName());
        }
        return cityNames;
    }

    public City getCityByName(String cityName) {
        return cityRepo.getFirstByName(cityName);
    }
}
