package by.vyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class GameRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private BoardGame game;
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
    private float rate;

    public GameRating(BoardGame game, User user, float rate) {
        this.game = game;
        this.user = user;
        this.rate = rate;
    }
}
