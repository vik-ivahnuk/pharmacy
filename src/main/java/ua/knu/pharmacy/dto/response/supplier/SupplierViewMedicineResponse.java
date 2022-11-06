package ua.knu.pharmacy.dto.response.supplier;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class SupplierViewMedicineResponse {
  Long id;
  String name;
  String description;
  BigDecimal price;
  LocalDate creationDate;
}
