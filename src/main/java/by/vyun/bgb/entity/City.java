package by.vyun.bgb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Data
@Entity
@ToString(exclude = {"gamers", "meetings"})
@EqualsAndHashCode(exclude = {"gamers", "meetings"})
public class City {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;
    private String logo;
    private String name;

    @OneToMany(mappedBy = "city", fetch = LAZY)
    private List<User> gamers;

    @OneToMany(mappedBy = "city", fetch = LAZY)
    private List<Meeting> meetings;

    public int getNumberOfMeetings() {
        if (meetings == null) {
            return 0;
        }
        return meetings.size();
    }

    public int getNumberOfGamers() {
        if (gamers == null) {
            return 0;
        }
        return gamers.size();
    }
}
