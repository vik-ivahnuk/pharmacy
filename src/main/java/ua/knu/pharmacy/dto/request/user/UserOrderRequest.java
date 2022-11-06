package ua.knu.pharmacy.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrderRequest {
  private Long id;
  private Integer count;
}
