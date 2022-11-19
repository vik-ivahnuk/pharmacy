package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.pharmacy.dto.request.admin.AdminAddMedicineRequest;
import ua.knu.pharmacy.dto.request.admin.AdminChangePriceMedicineRequest;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.exception.NotFoundException;
import ua.knu.pharmacy.repository.MedicineRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final MedicineRepository medicineRepository;

  public Long addMedicine(AdminAddMedicineRequest request) {
    return medicineRepository
        .save(
            Medicine.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .creationDate(LocalDate.now())
                .build())
        .getId();
  }

  @Transactional
  public Long changePriceMedicine(AdminChangePriceMedicineRequest request){
      Medicine medicine =
              medicineRepository.findById(request.getMedicine())
                      .orElseThrow( () -> new NotFoundException("No supplier with id = " + request.getMedicine()));
      medicine.setPrice(request.getPrice());
      return request.getMedicine();
  }

}
