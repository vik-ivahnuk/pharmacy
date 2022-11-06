package ua.knu.pharmacy.dto.request.supplier;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SupplierCreateMedicineBatchRequest {
  private Long supplierId;
  private List<SupplierCreateMedicineBatchItemRequest> items;
}
