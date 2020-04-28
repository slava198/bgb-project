package by.vyun.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private BoardGame game;
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
    private double gameRate = 0;
    private double userExperience = 0;
    private int completedMeets = 0;


//    public Rating(BoardGame game, User user, float gameRate) {
//        this.game = game;
//        this.user = user;
//        this.gameRate = gameRate;
//    }
}
