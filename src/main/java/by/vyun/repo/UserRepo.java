package by.vyun.repo;

import by.vyun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User, Integer> {

    //List<User> getAll();
    User getFirstByLogin(String login);
    User getFirstById(Integer id);

}
