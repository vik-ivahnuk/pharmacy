package ua.knu.pharmacy.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuppliedMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Medicine.class)
    @JoinColumn(nullable = false)
    private Medicine medicine;

    @Column(nullable = false)
    private LocalDate manufactureDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @ManyToOne(targetEntity = Supply.class)
    @JoinColumn(nullable = false)
    private Supply supply;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private BigDecimal price;

}
