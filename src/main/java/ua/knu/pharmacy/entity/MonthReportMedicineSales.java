package ua.knu.pharmacy.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.YearMonth;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthReportMedicineSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private YearMonth month;

    @ManyToOne(targetEntity = Medicine.class)
    @JoinColumn(nullable = false)
    private Medicine medicine;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private BigDecimal amount;

}
