package by.vyun.bgb.repository;

import by.vyun.bgb.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City, Integer> {
    City getFirstByName(String name);

}
