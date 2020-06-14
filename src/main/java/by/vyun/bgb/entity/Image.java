package by.vyun.bgb.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Base64;

import static javax.persistence.GenerationType.*;

@Data
@Entity
//@Table(name="images")
public class Image {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String name;
    @Lob
    private byte[] data;

    public String getPicture() {
        return Base64.getEncoder().encodeToString(data);
    }

}
