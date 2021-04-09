package uz.pdp.lesson4tasks.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Outcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Card fromCard;

    @OneToOne
    private Card toCard;

    private Double amount;

    @CreationTimestamp
    private Timestamp date;

    private Double commissionAmount;
}
