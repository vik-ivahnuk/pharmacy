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
import java.time.YearMonth;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
      private final UserRepository userRepository;
      private final MedicineRepository medicineRepository;
      private final DailyReportMedicineRepository dailyReportMedicineRepository;
      private final MedicineStockRepository medicineStockRepository;
      private final DailyProfitAndLossRepository dailyProfitAndLossRepository;
      private final MonthProfitAndLossRepository monthProfitAndLossRepository;
      private final MonthReportMedicineSalesRepository monthReportMedicineSalesRepository;
      private final  DailyCountSalesRepository dailyCountSalesRepository;
      private final OrderRepository orderRepository;
      private final OrderedMedicinesRepository orderedMedicinesRepository;
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
                .filter( medicineStock -> Objects.equals(medicineStock.getDate(), LocalDate.now()))
                .collect(Collectors.groupingBy(it -> it.getMedicine().getId()));
    }

    @Transactional
    public Boolean order(UserOrderRequest request) {
        Order order =
           orderRepository.save(
                Order.builder()
                    .user(
                        userRepository
                            .findById(request.getUserId())
                            .orElseThrow(
                                () -> new NotFoundException("No user with id = " + request.getUserId())))
                    .date(LocalDate.now())
                    .build());
        Map<Long, List<MedicineStock>> availableProducts = getAvailableProductsGroupingByMedicineId();
        for (UserOrderProductRequest r : request.getProducts()) {
            List<MedicineStock> medicineStockList = availableProducts.get(r.getId());
            if (medicineStockList == null) {
              throw new NotFoundException("Can not find medicine with id = " + r.getId());
            } if (medicineStockList.stream().map(MedicineStock::getCount).reduce(0, Integer::sum) < r.getCount()) {
              throw new NotFoundException("Can not find enough medicine with id = " + r.getId());
            }
        }

        List<OrderedMedicines> orderedMedicines =
                request.getProducts().stream()
                        .map(
                                value -> {
                                    return OrderedMedicines.builder()
                                            .medicine(medicineRepository.getReferenceById(value.getId()))
                                            .order(order)
                                            .price(medicineRepository.getReferenceById(value.getId()).getPrice())
                                            .count(value.getCount())
                                            .build();
                                }
                        )
                        .toList();
        orderedMedicinesRepository.saveAll(orderedMedicines);

        List<Integer> index ;
        BigDecimal profit = BigDecimal.ZERO;
        for (UserOrderProductRequest r : request.getProducts()) {
            Medicine medicine = medicineRepository.getReferenceById(r.getId());
            List<MonthReportMedicineSales> monthReport = monthReportMedicineSalesRepository.findAll().stream()
                    .filter(mr -> mr.getMedicine()==medicine)
                    .filter(mr-> Objects.equals(mr.getMonth(), YearMonth.now()))
                    .toList();

            BigDecimal amount = BigDecimal.valueOf(r.getCount())
                    .multiply(medicine.getPrice());
            if(monthReport.size()==0){
                monthReportMedicineSalesRepository.save(
                        MonthReportMedicineSales.builder()
                                .medicine(medicine)
                                .count(r.getCount())
                                .month(YearMonth.now())
                                .amount(amount)
                                .build()
                );
            } else {
                monthReport.get(0).setAmount(monthReport.get(0).getAmount().add(amount));
                monthReport.get(0).setCount(monthReport.get(0).getCount() + r.getCount());
            }
            profit =  profit.add(amount);
            index = new ArrayList<>();
            List<MedicineStock> medicineStockList = availableProducts.get(r.getId());
            medicineStockList = medicineStockList.stream()
                    .sorted(Comparator.comparing(MedicineStock::getExpirationDate)).toList();
            for(MedicineStock m: medicineStockList){
                List<DailyReportMedicine> reports =
                        dailyReportMedicineRepository.findAll().stream()
                                .filter(re-> Objects.equals(re.getDate(), LocalDate.now()))
                                .filter(re->re.getMedicine()==m.getMedicine())
                                .filter(re->re.getSupply()==m.getSupply().getSupply())
                                .filter(DailyReportMedicine::getIsSale)
                                .toList();
                Integer count = 0;
                Boolean is_break = false;
                if(r.getCount()<m.getCount()){
                    count = r.getCount();
                    m.setCount(m.getCount() - r.getCount());
                    is_break = true;
                } else {
                    count =  m.getCount();
                    r.setCount(r.getCount()- m.getCount());
                    index.add(medicineStockList.indexOf(m));
                    if(r.getCount()==0)
                        is_break = true;
                }

                if(reports.size()==0){
                    dailyReportMedicineRepository.save(DailyReportMedicine.builder()
                            .medicine(m.getMedicine())
                            .amount(BigDecimal.valueOf(count).multiply(m.getMedicine().getPrice()))
                            .date(LocalDate.now())
                            .supply(m.getSupply().getSupply())
                            .count(count)
                            .isSale(true)
                            .build());
                } else {
                    reports.get(0)
                            .setCount(reports.get(0).getCount()+count);
                    reports.get(0)
                            .setAmount(reports.get(0).getAmount().add(BigDecimal.valueOf(count).multiply(m.getMedicine().getPrice())));
                }
                if(is_break)
                    break;
            }

            for(Integer i: index){
                medicineStockRepository.delete(medicineStockList.get(i));
            }
        }
        updateProfit(LocalDate.now(), profit);
        updateCountSales(LocalDate.now());
        return true;
    }

    private void updateProfit(LocalDate date, BigDecimal amount){
        List<DailyProfitAndLoss> daily  = dailyProfitAndLossRepository.findAll().stream()
                .filter(d->Objects.equals(d.getDate(), date))
                .toList();
        YearMonth yearMonth = YearMonth.from(date);
        List<MonthProfitAndLoss> month = monthProfitAndLossRepository.findAll().stream()
                .filter(mo  -> Objects.equals(mo.getDate(), yearMonth))
                .toList();
        if(daily.size()==0) {
            dailyProfitAndLossRepository.save(
              DailyProfitAndLoss.builder()
                      .profit(amount)
                      .date(date)
                      .loss(BigDecimal.ZERO)
                      .build()
            );
        } else {
            daily.get(0).setProfit(daily.get(0).getProfit().add(amount));
        }

        if (month.size()==0){
            monthProfitAndLossRepository.save(
                    MonthProfitAndLoss.builder()
                            .profit(amount)
                            .date(yearMonth)
                            .loss(BigDecimal.ZERO)
                            .build()
            );
        } else {
            month.get(0).setProfit(month.get(0).getProfit().add(amount));
        }

    }

    private  void updateCountSales(LocalDate date){
        List<DailyCountSales> daily  = dailyCountSalesRepository.findAll().stream()
                .filter(d->Objects.equals(d.getDate(), date))
                .toList();
        if(daily.size()==0) {
            dailyCountSalesRepository.save(
                    DailyCountSales.builder()
                            .count(1)
                            .date(date)
                            .build()
            );
        } else {
            daily.get(0).setCount(daily.get(0).getCount()+1);
        }
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
