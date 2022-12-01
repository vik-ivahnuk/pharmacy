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
public class MedicineStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(targetEntity = Medicine.class)
    @JoinColumn(nullable = false)
    private Medicine medicine;

    @ManyToOne(targetEntity = SuppliedMedicine.class)
    @JoinColumn(nullable = false)
    private SuppliedMedicine supply;

    @Column(nullable = false)
    private Integer count;

    public LocalDate getExpirationDate(){
        return supply.getExpirationDate();
    }

    public BigDecimal getAmount(){
        return supply.getPrice().multiply(BigDecimal.valueOf(count));
    }

}
