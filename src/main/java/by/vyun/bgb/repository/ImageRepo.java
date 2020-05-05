package by.vyun.bgb.repository;

import by.vyun.bgb.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Image, Integer> {
    Image findFirstByName(String name);
    Image findFirstById(int id);


}
