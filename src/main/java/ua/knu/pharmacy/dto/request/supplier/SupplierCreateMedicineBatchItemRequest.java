package ua.knu.pharmacy.dto.request.supplier;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SupplierCreateMedicineBatchItemRequest {
  private Long medicineId;
  private BigDecimal count;
  private LocalDate manufactureDate;
  private LocalDate expirationDate;
}
