package ua.knu.pharmacy.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(targetEntity = Medicine.class)
    @JoinColumn(nullable = false)
    private Medicine medicine;

    @ManyToOne(targetEntity = Supply.class)
    @JoinColumn(nullable = false)
    private Supply supply;

    @Column(nullable = false)
    private Integer count;

}
