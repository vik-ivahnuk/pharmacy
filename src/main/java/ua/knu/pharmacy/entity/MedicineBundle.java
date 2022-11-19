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
public class MedicineBundle {
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

  @Column(nullable = false)
  private BigDecimal pricePaidSupplier;

  @Column
  private  BigDecimal priceToSell;

  @ManyToOne(targetEntity = MedicineBatch.class)
  @JoinColumn(nullable = false)
  private MedicineBatch medicineBatch;

  @ManyToOne(targetEntity = Order.class)
  private Order order;
}
