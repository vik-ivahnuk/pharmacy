package ua.knu.pharmacy.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private LocalDate creationDate;

  @OneToMany(mappedBy = "medicine")
  private List<Review> reviews = new ArrayList<>();
}
