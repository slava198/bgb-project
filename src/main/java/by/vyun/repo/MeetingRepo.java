package by.vyun.repo;

import by.vyun.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepo extends JpaRepository<Meeting, Integer> {

    Meeting getFirstById(int id);

}
