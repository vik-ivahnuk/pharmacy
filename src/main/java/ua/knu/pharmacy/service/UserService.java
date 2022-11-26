package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.dto.request.TimeRequest;
import ua.knu.pharmacy.dto.request.user.UserCreateUserRequest;
import ua.knu.pharmacy.dto.request.user.UserOrderProductRequest;
import ua.knu.pharmacy.dto.request.user.UserOrderRequest;
import ua.knu.pharmacy.dto.request.user.UserReviewRequest;
import ua.knu.pharmacy.dto.response.user.UserViewCostsOfMedicinesResponse;
import ua.knu.pharmacy.dto.response.user.UserViewHistoryResponse;
import ua.knu.pharmacy.dto.response.user.UserViewProductResponse;
import ua.knu.pharmacy.entity.*;
import ua.knu.pharmacy.exception.NotFoundException;
import ua.knu.pharmacy.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
      private final UserRepository userRepository;
      private final MedicineBundleRepository medicineBundleRepository;
      private final MedicineRepository medicineRepository;
      private final SuppliedMedicineRepository suppliedMedicineRepository;

      private final MedicineStockRepository medicineStockRepository;
      private final OrderRepository orderRepository;
      private final ReviewRepository reviewRepository;

      public Long registration(UserCreateUserRequest request) {
        return userRepository
            .save(User.builder().name(request.getName()).creationDate(LocalDate.now()).build())
            .getId();
      }

      @Transactional
      public List<UserViewProductResponse> getAvailableProducts() {
          return getAvailableProductsGroupingByMedicineId().values().stream()
                  .map(
                        value -> {
                              Medicine medicine = value.get(0).getMedicine();
                              return UserViewProductResponse.builder()
                                  .id(medicine.getId())
                                  .name(medicine.getName())
                                  .description(medicine.getDescription())
                                  .price(medicine.getPrice())
                                  .reviews(medicine.getReviews().stream().map(Review::getText).toList())
                                  .count(value.stream().map(MedicineStock::getCount)
                                          .reduce(0, Integer::sum))
                                  .build();
                        })
                  .toList();
      }

    private Map<Long, List<MedicineStock>> getAvailableProductsGroupingByMedicineId() {
        return medicineStockRepository.findAll().stream()
                .collect(Collectors.groupingBy(it -> it.getMedicine().getId()));
    }

  @Transactional
  public Boolean order(UserOrderRequest request) {
//    Order order =
//        orderRepository.save(
//            Order.builder()
//                .user(
//                    userRepository
//                        .findById(request.getUserId())
//                        .orElseThrow(
//                            () -> new NotFoundException("No user with id = " + request.getUserId())))
//                .date(LocalDate.now())
//                .build());
//    Map<Long, List<MedicineBundle>> availableProducts = getAvailableProductsGroupingByMedicineId();
//    for (UserOrderProductRequest r : request.getProducts()) {
//        List<MedicineBundle> medicineBundles = availableProducts.get(r.getId());
//        if (medicineBundles == null) {
//          throw new NotFoundException("Can not find medicine with id = " + r.getId());
//        }
//        if (medicineBundles.size() < r.getCount()) {
//          throw new NotFoundException("Can not find enough medicine with id = " + r.getId());
//        }
//        medicineBundles.stream()
//            .sorted(Comparator.comparing(MedicineBundle::getExpirationDate))
//            .limit(r.getCount())
//            .forEach(x -> {
//                x.setOrder(order);
//                x.setPriceToSell(x.getMedicine().getPrice());
//            });
//    }
    return true;
  }



  @Transactional
  public Long review(UserReviewRequest request) {
    User user =
        userRepository
            .findById(request.getUserId())
            .orElseThrow(() -> new NotFoundException("No user with id = " + request.getUserId()));
    Medicine medicine =
        medicineRepository
            .findById(request.getMedicineId())
            .orElseThrow(
                () -> new NotFoundException("No medicine with id = " + request.getUserId()));
    return reviewRepository
        .save(
            Review.builder()
                .user(user)
                .medicine(medicine)
                .text(request.getText())
                .date(LocalDate.now())
                .build())
        .getId();
  }

  @Transactional
  public UserViewHistoryResponse showHistory(LocalDate start, LocalDate end, Long user){
        List<UserViewHistoryResponse.OrderMedicines> orders = new ArrayList<>();
        List<Medicine> medicines = medicineRepository.findAll().stream().toList();
        BigDecimal total = BigDecimal.ZERO;
        while(start.isBefore(end.plusDays(1))){


            LocalDate finalStart = start;
            List<Order> medicineOrders =  orderRepository.findAll().stream()
                    .filter(order -> order.getDate().isEqual(finalStart))
                    .filter(order -> Objects.equals(order.getUser().getId(), user))
                    .toList();

            if(medicineOrders.size()==0){
                start = start.plusDays(1);
                continue;
            }

            for(Order order: medicineOrders){

                List<MedicineBundle> medicineBundles = order.getBundles().stream().toList();
                BigDecimal amount = medicineBundles.stream()
                        .map(MedicineBundle::getPriceToSell)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                total = total.add(amount);
                List<UserViewHistoryResponse.OrderMedicines.OrderedMedicine> orderedMedicines = new ArrayList<>();
                for(Medicine medicine: medicines){
                    String name = medicine.getName();
                    List<MedicineBundle> bundles =
                            medicineBundles.stream()
                                    .filter(medicineBundle -> Objects.equals(medicineBundle.getMedicine().getId(),
                                            medicine.getId()))
                                    .toList();
                    int count = bundles.size();
                    if(count==0)
                        continue;
                    BigDecimal price = bundles.get(0).getPriceToSell();
                    orderedMedicines.add(
                            UserViewHistoryResponse.OrderMedicines.OrderedMedicine.builder()
                                    .name(name)
                                    .count(count)
                                    .price(price)
                                    .build()
                    );
                }

                orders.add(UserViewHistoryResponse.OrderMedicines.builder()
                                .date(start)
                                .amount(amount)
                                .medicines(orderedMedicines)
                                .build());

            }

            start = start.plusDays(1);
        }

        return UserViewHistoryResponse.builder()
                .total(total)
                .orders(orders)
                .build();
  }

    @Transactional
    public UserViewCostsOfMedicinesResponse costsOfMedicines(LocalDate start, LocalDate end, Long userId){
      List<MedicineBundle> medicineBundles =
                orderRepository.findAll().stream()
                        .filter(order -> order.getDate().isBefore(end.plusDays(1))&&
                                start.minusDays(1).isBefore(order.getDate()))
                        .filter(order-> Objects.equals(order.getUser().getId(), userId))
                        .flatMap(order -> order.getBundles().stream())
                        .toList();
        BigDecimal total =medicineBundles.stream()
                .map(MedicineBundle::getPriceToSell)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        List<Medicine> medicines =
                medicineRepository.findAll().stream().toList();
        for(Medicine medicine : medicines){
            BigDecimal percent =
                medicineBundles.stream()
                        .filter(medicineBundle -> medicineBundle.getMedicine() == medicine)
                        .map(MedicineBundle::getPriceToSell)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

            if(percent.equals(BigDecimal.ZERO))
                continue;
            result.put(medicine.getName(),percent);
        }
        return UserViewCostsOfMedicinesResponse.builder()
                .total(total)
                .medicines(result)
                .build();
    }
}
