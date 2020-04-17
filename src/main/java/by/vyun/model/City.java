package by.vyun.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.*;

@Data
@Entity
public class City {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;
    private String logo;
    private String name;

}
