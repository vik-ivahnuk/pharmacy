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
@Table(name = "user_order")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(targetEntity = User.class)
  private User user;

  @Column(nullable = false)
  private LocalDate date;

  @OneToMany(mappedBy = "order")
  private List<OrderedMedicines> orderedMedicines = new ArrayList<>();
}
