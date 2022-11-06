package ua.knu.pharmacy.dto.response.user;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserViewProductResponse {
  UserViewMedicineResponse info;
  LocalDate manufactureDate;
  LocalDate expirationDate;
  Integer count;
}
