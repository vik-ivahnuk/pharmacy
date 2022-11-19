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
@Table(name = "user_client")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private LocalDate creationDate;

  @OneToMany(mappedBy = "user")
  private List<Review> reviews = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Order> orders = new ArrayList<>();
}
