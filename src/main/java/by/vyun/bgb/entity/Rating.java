package by.vyun.bgb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Data
@Entity
@NoArgsConstructor
//@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private BoardGame game;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private double gameRate = 0;
    private double userExperience = 0;
    private int completedMeets = 0;

}
