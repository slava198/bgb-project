package by.vyun.bgb.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static javax.persistence.GenerationType.*;


@Entity
@Data
@ToString(exclude = {"gameCollection", "meetingSet", "createdMeets", "ratings"})
@EqualsAndHashCode(exclude = {"gameCollection", "meetingSet", "createdMeets", "ratings"})
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@userId")
public class User {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;
    private String login;
    private String password;
    private String address;
    private String email;
    private Boolean isActive = true;
    private Boolean isEnabled = false;
    private long activationCode;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image avatar;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @ManyToOne()
    @JoinColumn(name = "cityId")
    private City city;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = Collections.singleton("ROLE_USER");

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "games_owners",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "game_id")}
    )
    private List<BoardGame> gameCollection;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meetings_members",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "meeting_id")}
    )
    private List<Meeting> meetingSet;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Meeting> createdMeets;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Rating> ratings;


    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }


    public int getCompletedMeetsNumber() {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }
        int num = 0;
        for (Rating rating : ratings) {
            num += rating.getCompletedMeets();
        }
        return num;
    }

    public double getTotalExperience() {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }
        double us_ex = 0;
        for (Rating rating : ratings) {
            us_ex += rating.getUserExperience();
        }
        return us_ex;
    }

    public double getExperienceInGame(BoardGame game) {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }
        for (Rating rating : ratings) {
            if (rating.getGame() == game) {
                return rating.getUserExperience();
            }
        }
        return 0;
    }

    public void addExperienceInGame(BoardGame game, double experience) {
        Rating rating = getRatingInGame(game);
        if (rating == null) {
            rating = new Rating();
            rating.setUser(this);
            rating.setGame(game);
            ratings.add(rating);
        }
        rating.setUserExperience(rating.getUserExperience() + experience);
    }


    public Rating getRatingInGame(BoardGame game) {
        if (ratings.isEmpty()) {
            return null;
        }
        for (Rating rating : ratings) {
            if (rating.getGame() == game) {
                return rating;
            }
        }
        return null;
    }


    public boolean checkPassword(String password) {
        return !this.getPassword().equals(password);
    }

    public void addGameToCollection(BoardGame game) {
        gameCollection.add(game);
    }

    public void deleteGameFromCollection(BoardGame game) {
        gameCollection.remove(game);
    }

    public void addMeeting(Meeting meeting) {
        meetingSet.add(meeting);
    }

    public void leaveMeeting(Meeting meeting) {
        meetingSet.remove(meeting);
    }

    public void deleteMeeting(Meeting meeting) {
        meetingSet.remove(meeting);
        createdMeets.remove(meeting);
    }

}
