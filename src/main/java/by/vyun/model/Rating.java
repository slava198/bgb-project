package by.vyun.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Data
@Entity
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = IDENTITY)
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

}
