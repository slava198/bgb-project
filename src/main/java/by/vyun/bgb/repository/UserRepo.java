package by.vyun.bgb.repository;

import by.vyun.bgb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User getFirstByLogin(String login);
    User getFirstById(Long id);

}
