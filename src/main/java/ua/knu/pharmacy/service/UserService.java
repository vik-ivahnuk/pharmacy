package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.pharmacy.dto.response.user.UserViewMedicineResponse;
import ua.knu.pharmacy.dto.response.user.UserViewProductResponse;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.entity.MedicineBundle;
import ua.knu.pharmacy.repository.MedicineBundleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
  private final MedicineBundleRepository medicineBundleRepository;

  public List<UserViewProductResponse> getAvailableProducts() {
    return medicineBundleRepository.findAll().stream()
        .filter(it -> !it.getExpirationDate().isBefore(LocalDate.now()))
        .filter(it -> it.getSaleDate() == null)
        .collect(
            Collectors.groupingBy(
                it ->
                    Map.of(
                        "medicine_id",
                        it.getMedicine().getId(),
                        "manufacture_date",
                        it.getManufactureDate(),
                        "expiration_date",
                        it.getExpirationDate())))
        .values()
        .stream()
        .map(
            value -> {
              MedicineBundle element = value.get(0);
              Medicine medicine = element.getMedicine();
              return UserViewProductResponse.builder()
                  .info(
                      UserViewMedicineResponse.builder()
                          .id(medicine.getId())
                          .name(medicine.getName())
                          .description(medicine.getDescription())
                          .build())
                  .manufactureDate(element.getManufactureDate())
                  .expirationDate(element.getExpirationDate())
                  .count(value.size())
                  .build();
            })
        .toList();
  }
}
