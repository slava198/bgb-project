package by.vyun.bgb.repository;

import by.vyun.bgb.entity.MeetingResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingResultRepo extends JpaRepository<MeetingResult, Integer> {
    List<MeetingResult> getAllByMeetId(Integer id);

}
