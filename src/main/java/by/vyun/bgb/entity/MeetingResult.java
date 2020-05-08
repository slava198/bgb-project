package by.vyun.bgb.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class MeetingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meet_id")
    private Meeting meet;
    @OneToOne
    @JoinColumn(name = "user_from_id")
    private User from;
    @OneToOne
    @JoinColumn(name = "user_to_id")
    private User to;
    private double points;

}