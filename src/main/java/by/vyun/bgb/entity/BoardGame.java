package by.vyun.bgb.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"owners", "meetings", "ratings"})
@EqualsAndHashCode(exclude = {"owners", "meetings", "ratings"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@boardGameId")
public class BoardGame {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String logo;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer age = 0;
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "games_owners",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> owners;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private Set<Meeting> meetings;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private List<Rating> ratings;

    @Transactional
    public float getRatingValue() {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }
        float ratingSum = 0;
        int ratingNum = 0;
        for (Rating rating : ratings) {
            double rate = rating.getGameRate();
            if (rate != 0) {
                ratingSum += rate;
                ratingNum++;
            }
        }
        return ratingSum / ratingNum;
    }

    public Integer getNumberOfOwners() {
        if (owners == null) {
            return 0;
        }
        return owners.size();
    }

    public Integer getNumberOfMeetings() {
        if (meetings == null) {
            return 0;
        }
        return meetings.size();
    }

}
