package by.vyun.bgb.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"members"})
@EqualsAndHashCode(exclude = {"members"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@meetingId")
//@Table(name="meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = AUTO)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "city_id")
    private City city;
    @NotBlank(message = "Empty location field")
    private String location;

    @NotNull(message = "Invalid date field")
    //@Future(message = "Invalid date field")
    // block resaving meets after change status)))) todo: add this validation in createMeetDto
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne()
    @JoinColumn(name = "game_id")
    private BoardGame game;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meetings_members",
            joinColumns = {@JoinColumn(name = "meeting_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> members;

    @OneToMany(mappedBy = "meet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MeetingResult> results;

    private MeetingState state = MeetingState.Created;


    public int getNumberOfMembers() {
        if (members == null) {
            return 0;
        }
        return members.size();
    }

}
