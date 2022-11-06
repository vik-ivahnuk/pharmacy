package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.dto.request.user.UserCreateUserRequest;
import ua.knu.pharmacy.dto.request.user.UserOrderRequest;
import ua.knu.pharmacy.dto.response.user.UserViewProductResponse;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.entity.MedicineBundle;
import ua.knu.pharmacy.entity.User;
import ua.knu.pharmacy.exception.NotFoundException;
import ua.knu.pharmacy.repository.MedicineBundleRepository;
import ua.knu.pharmacy.repository.UserRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final MedicineBundleRepository medicineBundleRepository;

  public Long registration(UserCreateUserRequest request) {
    return userRepository
        .save(User.builder().name(request.getName()).creationDate(LocalDate.now()).build())
        .getId();
  }

  public List<UserViewProductResponse> getAvailableProducts() {
    return getAvailableProductsGroupingByMedicineId().values().stream()
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

  @Transactional
  public void order(List<UserOrderRequest> requests) {
    Map<Long, List<MedicineBundle>> availableProducts = getAvailableProductsGroupingByMedicineId();
    for (UserOrderRequest r : requests) {
      List<MedicineBundle> medicineBundles = availableProducts.get(r.getId());
      if (medicineBundles == null) {
        throw new NotFoundException("Can not find medicine with id = " + r.getId());
      }
      if (medicineBundles.size() < r.getCount()) {
        throw new NotFoundException("Can not find enough medicine with id = " + r.getId());
      }
      medicineBundles.stream()
          .sorted(Comparator.comparing(MedicineBundle::getExpirationDate))
          .limit(r.getCount())
          .forEach(x -> x.setSaleDate(LocalDate.now()));
    }
  }

  private Map<Long, List<MedicineBundle>> getAvailableProductsGroupingByMedicineId() {
    return medicineBundleRepository.findAll().stream()
        .filter(it -> !it.getExpirationDate().isBefore(LocalDate.now()))
        .filter(it -> it.getSaleDate() == null)
        .collect(Collectors.groupingBy(it -> it.getMedicine().getId()));
  }
}
