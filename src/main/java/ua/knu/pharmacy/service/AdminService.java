package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.pharmacy.dto.request.admin.AdminAddMedicineRequest;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.repository.MedicineRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final MedicineRepository medicineRepository;

  public void addMedicine(AdminAddMedicineRequest request) {
    medicineRepository.save(
        Medicine.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .creationDate(LocalDate.now())
            .build());
  }
}
