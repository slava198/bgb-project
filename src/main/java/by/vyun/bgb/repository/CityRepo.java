package by.vyun.bgb.repository;

import by.vyun.bgb.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;

public interface CityRepo extends JpaRepository<City, Long> {
    City getFirstByName(String name);
    City getFirstById(Long id);

}

