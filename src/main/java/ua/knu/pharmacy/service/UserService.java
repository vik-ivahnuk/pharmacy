package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.pharmacy.dto.response.user.UserViewProductResponse;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.repository.MedicineBundleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
  private final MedicineBundleRepository medicineBundleRepository;

  public List<UserViewProductResponse> getAvailableProducts() {
    return medicineBundleRepository.findAll().stream()
        .filter(it -> !it.getExpirationDate().isBefore(LocalDate.now()))
        .filter(it -> it.getSaleDate() == null)
        .collect(Collectors.groupingBy(it -> it.getMedicine().getId()))
        .values()
        .stream()
        .map(
            value -> {
              Medicine medicine = value.get(0).getMedicine();
              return UserViewProductResponse.builder()
                  .id(medicine.getId())
                  .name(medicine.getName())
                  .description(medicine.getDescription())
                  .count(value.size())
                  .build();
            })
        .toList();
  }
}
