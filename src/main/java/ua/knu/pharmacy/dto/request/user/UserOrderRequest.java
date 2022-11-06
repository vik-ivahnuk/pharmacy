package ua.knu.pharmacy.dto.request.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserOrderRequest {
  private Long userId;
  private List<UserOrderProductRequest> products;
}
