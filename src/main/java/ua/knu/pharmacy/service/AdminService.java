package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.pharmacy.dto.request.admin.AdminAddMedicineRequest;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateSupplierRequest;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.entity.Supplier;
import ua.knu.pharmacy.repository.MedicineRepository;
import ua.knu.pharmacy.repository.SupplierRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final MedicineRepository medicineRepository;
  private final SupplierRepository supplierRepository;

  public void addMedicine(AdminAddMedicineRequest request) {
    medicineRepository.save(
        Medicine.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .creationDate(LocalDate.now())
            .build());
  }

  public void addSupplier(SupplierCreateSupplierRequest request) {
    supplierRepository.save(
        Supplier.builder().name(request.getName()).creationDate(LocalDate.now()).build());
  }
}
