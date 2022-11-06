package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.pharmacy.dto.response.supplier.SupplierViewMedicineResponse;
import ua.knu.pharmacy.repository.MedicineRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
  private final MedicineRepository medicineRepository;

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
}
