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
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(targetEntity = User.class)
  private User user;

  @ManyToOne(targetEntity = Medicine.class)
  private Medicine medicine;

  @Column(nullable = false)
  private String text;

  @Column(nullable = false)
  private LocalDate date;
}
