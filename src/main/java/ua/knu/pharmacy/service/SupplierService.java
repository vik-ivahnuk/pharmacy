package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateSupplierRequest;
import ua.knu.pharmacy.dto.response.supplier.SupplierViewMedicineResponse;
import ua.knu.pharmacy.entity.Supplier;
import ua.knu.pharmacy.repository.MedicineRepository;
import ua.knu.pharmacy.repository.SupplierRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
  private final MedicineRepository medicineRepository;
  private final SupplierRepository supplierRepository;

  public List<SupplierViewMedicineResponse> availableMedicines() {
    return medicineRepository.findAll().stream()
        .map(
            x ->
                SupplierViewMedicineResponse.builder()
                    .id(x.getId())
                    .name(x.getName())
                    .description(x.getDescription())
                    .price(x.getPrice())
                    .creationDate(x.getCreationDate())
                    .build())
        .toList();
  }

  public void addSupplier(SupplierCreateSupplierRequest request) {
    supplierRepository.save(
        Supplier.builder().name(request.getName()).creationDate(LocalDate.now()).build());
  }
}
