package by.vyun.repo;

import by.vyun.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City, Integer> {
    City getFirstByName(String name);

}
