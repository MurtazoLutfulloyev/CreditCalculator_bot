package uz.asaka.creditcalculator_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "credits")
@Component
public class Credit {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "total_amount")
    private Long totalAmount;

    private double duration;

    private double percentage;

    @ManyToOne
    private User user;

    private String filePath;

}
