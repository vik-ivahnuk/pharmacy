package ua.knu.pharmacy.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdminAddMedicineRequest {
  private String name;
  private String description;
  private BigDecimal price;
}
