package by.vyun.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String logo;
    private String title;
    private String description;
    private Integer age = 0;
    //private float rating = 0;
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

    public float getRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }
        float x = 0;
        for (Rating rating: ratings) {
            x += rating.getGameRate();
        }
        return x / ratings.size();
    }

    public Integer getNumberOfOwners() {
        if(owners == null) {
            return 0;
        }
        return owners.size();
    }

    public Integer getNumberOfMeetings() {
        if(meetings == null) {
            return 0;
        }
        return meetings.size();
    }


}
