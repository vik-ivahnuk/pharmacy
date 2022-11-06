package ua.knu.pharmacy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @ManyToOne(targetEntity = MedicineBatch.class)
  @JoinColumn(nullable = false)
  private MedicineBatch medicineBatch;
}
