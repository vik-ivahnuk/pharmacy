package ua.knu.pharmacy.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReviewRequest {
  private Long userId;
  private Long medicineId;
  private String text;
}
