package by.vyun.repo;

import by.vyun.model.MeetingResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingResultRepo extends JpaRepository<MeetingResult, Integer> {
    List<MeetingResult> getAllByMeetId(Integer id);

    void deleteByMeetId(int id);

}
