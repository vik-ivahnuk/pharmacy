package ua.knu.pharmacy.dto.request.supplier;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SupplierCreateMedicineBatchItemRequest {
  private Long medicineId;
  private Integer count;
  private LocalDate manufactureDate;
  private LocalDate expirationDate;
  private BigDecimal pricePaidSupplier;
}
