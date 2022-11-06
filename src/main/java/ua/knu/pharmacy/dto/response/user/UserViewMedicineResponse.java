package ua.knu.pharmacy.dto.response.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserViewMedicineResponse {
  Long id;
  String name;
  String description;
}
