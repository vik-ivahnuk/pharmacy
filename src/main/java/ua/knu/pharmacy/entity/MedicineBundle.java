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
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineBundle {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private LocalDate manufactureDate;

  @Column(nullable = false)
  private LocalDate expirationDate;

  @ManyToOne(targetEntity = Medicine.class)
  @JoinColumn(nullable = false)
  private Medicine medicine;

  @ManyToOne(targetEntity = MedicineBatch.class)
  @JoinColumn(nullable = false)
  private MedicineBatch medicineBatch;
}
