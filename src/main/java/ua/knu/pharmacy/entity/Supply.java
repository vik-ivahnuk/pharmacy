package ua.knu.pharmacy.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Supplier.class)
    @JoinColumn(nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private LocalDate supplyDate;

    @OneToMany(mappedBy = "supply")
    private List<SuppliedMedicine> suppliedMedicines = new ArrayList<>();
}
