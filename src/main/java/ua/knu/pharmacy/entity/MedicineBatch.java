package ua.knu.pharmacy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineBatch {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private BigInteger amount;

  @Column(nullable = false)
  private LocalDate supplyDate;

  @ManyToOne(targetEntity = Supplier.class)
  @JoinColumn(nullable = false)
  private Supplier supplier;

  @OneToMany(mappedBy = "medicineBatch")
  private List<MedicineBundle> bundles = new ArrayList<>();
}
