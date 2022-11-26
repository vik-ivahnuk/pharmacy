package ua.knu.pharmacy.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderedMedicines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Medicine.class)
    @JoinColumn(nullable = false)
    private Medicine medicine;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(nullable = false)
    private Order order;

}
