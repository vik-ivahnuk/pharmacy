package ua.knu.pharmacy.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrderProductRequest {
  private Long id;
  private Integer count;
}
