package by.vyun.bgb.repository;

import by.vyun.bgb.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepo extends JpaRepository<Meeting, Integer> {
    Meeting getFirstById(int id);

}
