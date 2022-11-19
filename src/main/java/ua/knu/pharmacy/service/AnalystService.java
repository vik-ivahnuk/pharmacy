package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.dto.response.analyst.*;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.entity.MedicineBundle;
import ua.knu.pharmacy.entity.Order;
import ua.knu.pharmacy.repository.MedicineBatchRepository;
import ua.knu.pharmacy.repository.MedicineBundleRepository;
import ua.knu.pharmacy.repository.MedicineRepository;
import ua.knu.pharmacy.repository.OrderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalystService {
    private final MedicineBundleRepository medicineBundleRepository;

    private final MedicineBatchRepository medicineBatchRepository;

    private final MedicineRepository medicineRepository;

    private final OrderRepository orderRepository;

    @Transactional
    public AnalystViewResponse analysePerDay(LocalDate date) {
      List<MedicineBundle> supplied =
          medicineBatchRepository.findAll().stream()
              .filter(batch -> batch.getSupplyDate().isEqual(date))
              .flatMap(batch -> batch.getBundles().stream())
              .toList();
      List<MedicineBundle> ordered =
          medicineBundleRepository.findAll().stream()
              .filter(bundle -> bundle.getOrder() != null)
              .filter(bundle -> bundle.getOrder().getDate().isEqual(date))
              .toList();
      List<MedicineBundle> expired =
          medicineBundleRepository.findAll().stream()
              .filter(bundle -> bundle.getOrder() == null)
              .filter(bundle -> bundle.getExpirationDate().isEqual(date))
              .toList();
      return AnalystViewResponse.builder()
          .supplied(viewMedicine(supplied, false))
          .ordered(viewMedicine(ordered, true))
          .expired(viewMedicine(expired, false))
          .build();
    }


    @Transactional
    public AnalystViewResponse salesOfButch(Long Butch){

        List<MedicineBundle> supplied =
                medicineBundleRepository.findAll().stream()
                        .filter(bundle -> Objects.equals(bundle.getMedicineBatch().getId(), Butch))
                        .toList();

        List<MedicineBundle> ordered =
                medicineBundleRepository.findAll().stream()
                        .filter(bundle -> bundle.getOrder() != null)
                        .filter(bundle -> Objects.equals(bundle.getMedicineBatch().getId(), Butch))
                        .toList();
        List<MedicineBundle> expired =
                medicineBundleRepository.findAll().stream()
                        .filter(bundle -> bundle.getOrder() == null)
                        .filter(bundle -> bundle.getExpirationDate().isBefore(LocalDate.now()))
                        .toList();

        return AnalystViewResponse.builder()
                .supplied(viewMedicine(supplied, false))
                .ordered(viewMedicine(ordered, true))
                .expired(viewMedicine(expired, false))
                .build();
    }

    @Transactional
    public AnalystStocksButchResponse stocksButch(Long Butch){
        List<MedicineBundle> stocks =
                medicineBundleRepository.findAll().stream()
                        .filter(bundle -> bundle.getOrder() == null)
                        .filter(bundle -> !bundle.getExpirationDate().isBefore(LocalDate.now()))
                        .filter(bundle -> Objects.equals(bundle.getMedicineBatch().getId(), Butch))
                        .toList();
        return AnalystStocksButchResponse.builder()
                .stocks(viewMedicine(stocks, false))
                .build();

    }

    private AnalystViewResponse.AnalystViewPartResponse viewMedicine(List<MedicineBundle> data, Boolean isSell) {

        return AnalystViewResponse.AnalystViewPartResponse.builder()
                .amount(
                        data.stream()
                                .map(isSell ? MedicineBundle::getPriceToSell :
                                        MedicineBundle::getPricePaidSupplier)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                .items(
                        data.stream()
                                .collect(Collectors.groupingBy(it1 -> it1.getMedicine().getName()))
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size())))
                .build();
    }

    @Transactional
    public AnalystSalesMedicineResponse salesMedicines(Long medicine, LocalDate start, LocalDate end){


        List<MedicineBundle> ordered =
                medicineBundleRepository.findAll().stream()
                        .filter(medicineBundle1 -> Objects.equals(medicineBundle1.getMedicine().getId(), medicine))
                        .filter(medicineBundle -> medicineBundle.getOrder() != null)
                        .filter(medicineBundle -> medicineBundle.getOrder().getDate().isBefore(end.plusDays(1))&&
                                start.minusDays(1).isBefore(medicineBundle.getOrder().getDate()))
                        .toList();

        List<MedicineBundle> expired =
                medicineBundleRepository.findAll().stream()
                        .filter(medicineBundle1 -> Objects.equals(medicineBundle1.getMedicine().getId(), medicine))
                        .filter(medicineBundle -> medicineBundle.getOrder() == null)
                        .filter(medicineBundle -> medicineBundle.getExpirationDate().isBefore(end.plusDays(1))&&
                                start.minusDays(1).isBefore(medicineBundle.getExpirationDate()))
                        .toList();

        return AnalystSalesMedicineResponse.builder()
                .ordered(viewMedicine2(ordered,true))
                .expired(viewMedicine2(expired,false))
                .build();
    }

    @Transactional
    public AnalystStockMedicineResponse stockMedicines(Long medicine){


        List<MedicineBundle> stock =
                medicineBundleRepository.findAll().stream()
                        .filter(medicineBundle1 -> Objects.equals(medicineBundle1.getMedicine().getId(), medicine))
                        .filter(medicineBundle -> medicineBundle.getOrder() == null)
                        .filter(medicineBundle -> !medicineBundle.getExpirationDate().isBefore(LocalDate.now()))
                        .toList();

        return AnalystStockMedicineResponse.builder()
                .stock(viewMedicine2(stock,false))
                .build();
    }

    private AnalystSalesMedicineResponse.AnalystPartSalesMedicineResponse viewMedicine2(List<MedicineBundle> data, Boolean isSell) {

        return AnalystSalesMedicineResponse.AnalystPartSalesMedicineResponse.builder()
                .amount( data.stream()
                        .map(isSell ? MedicineBundle::getPriceToSell :
                                MedicineBundle::getPricePaidSupplier)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .count(data.size())
                .build();
    }

    @Transactional
    public AnalystMedicineSalesStatisticsResponse medicineSalesStatistics(LocalDate start, LocalDate end, Long medicine){
        List<AnalystMedicineSalesStatisticsResponse.salesPerDay> sales = new ArrayList<>();
        while(start.isBefore(end.plusDays(1))){
            sales.add(medicineSalesPerDay(start, medicine));
            start = start.plusDays(1);
        }
        return AnalystMedicineSalesStatisticsResponse.builder()
                .sales(sales)
                .build();
    }

    @Transactional
    public AnalystMedicineSalesStatisticsResponse.salesPerDay medicineSalesPerDay(LocalDate date, Long medicine){

         List<MedicineBundle> medicineBundles =
               orderRepository.findAll().stream()
                       .filter(order -> order.getDate().isEqual(date))
                       .flatMap(order -> order.getBundles().stream())
                       .filter(medicineBundle -> Objects.equals(medicineBundle.getMedicine().getId(), medicine))
                       .toList();

         return AnalystMedicineSalesStatisticsResponse.salesPerDay.builder()
                 .count(medicineBundles.size())
                 .date(date)
                 .amount(
                     medicineBundles.stream()
                             .map(MedicineBundle::getPriceToSell)
                             .reduce(BigDecimal.ZERO, BigDecimal::add))
                 .build();
    }

    @Transactional
    public AnalystDistributionSalesMedicineResponse DistributionSalesMedicine(LocalDate start, LocalDate end){
        List<Medicine> medicines = medicineRepository.findAll().stream().toList();
        return AnalystDistributionSalesMedicineResponse.builder()
                .sales(
                        medicines.stream()
                                .collect(Collectors.groupingBy(Medicine::getName))
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e-> {
                                    Long id = e.getValue().get(0).getId();
                                    return countSales(id, start, end);
                                }))
                )
                .build();
    }


    private Integer countSales(Long id, LocalDate start, LocalDate end){
        return Math.toIntExact(medicineBundleRepository.findAll().stream()
                .filter(medicineBundle -> Objects.equals(medicineBundle.getMedicine().getId(), id))
                .filter(medicineBundle -> medicineBundle.getOrder()!=null)
                .filter(medicineBundle -> medicineBundle.getOrder().getDate().isBefore(end.plusDays(1))&&
                        start.minusDays(1).isBefore(medicineBundle.getOrder().getDate()))
                .count());
    }

    @Transactional
    public AnalystProfitAndLossResponse profitAndLossAnalyse(LocalDate start, LocalDate end){
        return AnalystProfitAndLossResponse.builder()
                .profit(orderRepository.findAll().stream()
                    .filter(order -> order.getDate().isBefore(end.plusDays(1))&&
                                    start.minusDays(1).isBefore(order.getDate()))
                            .flatMap(order-> order.getBundles().stream())
                            .map(MedicineBundle::getPriceToSell)
                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                .loss(medicineBundleRepository.findAll().stream()
                        .filter(medicineBundle -> !medicineBundle.getExpirationDate().isBefore(LocalDate.now()))
                        .filter(medicineBundle -> medicineBundle.getMedicineBatch().getSupplyDate().isBefore(end.plusDays(1))&&
                                start.minusDays(1).isBefore(medicineBundle.getMedicineBatch().getSupplyDate()))
                        .map(MedicineBundle::getPricePaidSupplier)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .build();
    }

    @Transactional
    public AnalystAverageCheckResponse averageCheck(LocalDate start, LocalDate end){
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getDate().isBefore(end.plusDays(1))&&
                        start.minusDays(1).isBefore(order.getDate()))
                .toList();
        BigDecimal amount  = orders.stream()
                .flatMap(order -> order.getBundles().stream())
                .map(MedicineBundle::getPriceToSell)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(amount);
        return AnalystAverageCheckResponse.builder()
                .average(amount.divide(BigDecimal.valueOf(orders.size()), RoundingMode.HALF_UP))
                .build();
    }

    //public void


}
