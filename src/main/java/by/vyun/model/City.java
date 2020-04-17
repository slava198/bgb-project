package by.vyun.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String logo;
    String name;

}
