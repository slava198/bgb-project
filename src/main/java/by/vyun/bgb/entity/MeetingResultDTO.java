package by.vyun.bgb.entity;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class MeetingResultDTO {
    public List<MeetingResultElement> results;
    public MeetingResultDTO() {
        results = new ArrayList<>();
    }

}
