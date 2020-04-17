package by.vyun.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"members"})
@EqualsAndHashCode(exclude = {"members"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@meetingId")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @ManyToOne()
    @JoinColumn(name = "cityId")
    City city;

    String location;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "creatorId")
    User creator;

    @ManyToOne()
    @JoinColumn(name = "game_id")
    BoardGame game;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meetings_members",
            joinColumns = {@JoinColumn(name = "meeting_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    List<User> members;

    public int getNumberOfMembers() {
        if(members == null) {
            return 0;
        }
        return members.size();
    }




}
