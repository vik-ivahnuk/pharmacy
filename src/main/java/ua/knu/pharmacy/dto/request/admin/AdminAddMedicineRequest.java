package ua.knu.pharmacy.dto.request.admin;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class AdminAddMedicineRequest {
  String name;
  String description;
  BigDecimal price;
}
