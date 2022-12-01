package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.dto.response.analyst.*;
import ua.knu.pharmacy.entity.*;
import ua.knu.pharmacy.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalystService {
    private final MedicineRepository medicineRepository;
    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final DailyReportMedicineRepository dailyReportMedicineRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final DailyProfitAndLossRepository dailyProfitAndLossRepository;
    private final MonthProfitAndLossRepository monthProfitAndLossRepository;
    private final MonthReportMedicineSalesRepository monthReportMedicineSalesRepository;
    private final  DailyCountSalesRepository dailyCountSalesRepository;
    private final SuppliedMedicineRepository suppliedMedicineRepository;


    @Transactional
    public AnalystStocksSupplyResponse stocksSupply(Long supply, LocalDate date){

        return AnalystStocksSupplyResponse.builder()
                .stock(
                        medicineStockRepository.findAll().stream()
                                .filter(m -> Objects.equals(m.getSupply().getSupply().getId(),supply))
                                .filter(m -> Objects.equals(m.getDate(), date))
                                .toList().stream()
                                .collect(Collectors.groupingBy(it1 -> it1.getMedicine().getName()))
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                                        .map(MedicineStock::getCount)
                                        .reduce(0,Integer::sum)))
                )
                .build();
    }

    @Transactional
    public AnalystViewResponse salesOfSupplyByDay(Long supply, LocalDate date){
        List<DailyReportMedicine> ordered  =
                dailyReportMedicineRepository.findAll().stream()
                        .filter(o->Objects.equals(o.getDate(), date) && o.getIsSale())
                        .filter(o->Objects.equals(o.getSupply().getId(), supply))
                        .toList();
        List<DailyReportMedicine> expired  =
                dailyReportMedicineRepository.findAll().stream()
                        .filter(o->Objects.equals(o.getDate(), date) && !o.getIsSale())
                        .filter(o->Objects.equals(o.getSupply().getId(), supply))
                        .toList();
        List<SuppliedMedicine> supplied =
                suppliedMedicineRepository.findAll().stream()
                        .filter(s->Objects.equals(s.getSupply().getSupplyDate(), date))
                        .filter(s->Objects.equals(s.getSupply().getId(), supply))
                        .toList();


        return AnalystViewResponse.builder()
                .supplied(calculateSupplied(supplied))
                .ordered(calculateSales(ordered))
                .expired(calculateSales(expired))
                .build();
    }

    @Transactional
    public AnalystViewResponse salesOfSupplyByPeriod(Long supply, LocalDate start, LocalDate end){
        List<DailyReportMedicine> ordered  =
                dailyReportMedicineRepository.findAll().stream()
                        .filter(o->!end.isBefore(o.getDate()) &&
                                !o.getDate().isBefore(start) &&
                                o.getIsSale())
                        .filter(o->Objects.equals(o.getSupply().getId(), supply))
                        .toList();
        List<DailyReportMedicine> expired  =
                dailyReportMedicineRepository.findAll().stream()
                        .filter(o->!end.isBefore(o.getDate()) &&
                                !o.getDate().isBefore(start) &&
                                !o.getIsSale())
                        .filter(o->Objects.equals(o.getSupply().getId(), supply))
                        .toList();
        List<SuppliedMedicine> supplied =
                suppliedMedicineRepository.findAll().stream()
                        .filter(s->!end.isBefore(s.getSupply().getSupplyDate()) &&
                                !s.getSupply().getSupplyDate().isBefore(start) )
                        .filter(s->Objects.equals(s.getSupply().getId(), supply))
                        .toList();


        return AnalystViewResponse.builder()
                .supplied(calculateSupplied(supplied))
                .ordered(calculateSales(ordered))
                .expired(calculateSales(expired))
                .build();
    }

    @Transactional
    public AnalystViewResponse salesOfSupplyByMonth(Long supply, YearMonth date){
        List<DailyReportMedicine> ordered  =
                dailyReportMedicineRepository.findAll().stream()
                        .filter(o->Objects.equals(YearMonth.from(o.getDate()), date) && o.getIsSale())
                        .filter(o->Objects.equals(o.getSupply().getId(), supply))
                        .toList();
        List<DailyReportMedicine> expired  =
                dailyReportMedicineRepository.findAll().stream()
                        .filter(o->Objects.equals(YearMonth.from(o.getDate()), date) && !o.getIsSale())
                        .filter(o->Objects.equals(o.getSupply().getId(), supply))
                        .toList();
        List<SuppliedMedicine> supplied =
                suppliedMedicineRepository.findAll().stream()
                        .filter(s->Objects.equals(YearMonth.from(s.getSupply().getSupplyDate()), date))
                        .filter(s->Objects.equals(s.getSupply().getId(), supply))
                        .toList();


        return AnalystViewResponse.builder()
                .supplied(calculateSupplied(supplied))
                .ordered(calculateSales(ordered))
                .expired(calculateSales(expired))
                .build();
    }

    private AnalystViewResponse.AnalystViewPartResponse calculateSales(List<DailyReportMedicine> list){
        return AnalystViewResponse.AnalystViewPartResponse.builder()
                .amount(
                        list.stream()
                                .map(DailyReportMedicine::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .items(
                    list.stream()
                    .collect(Collectors.groupingBy(it1 -> it1.getMedicine().getName()))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                            .map(DailyReportMedicine::getCount)
                            .reduce(0,Integer::sum)))
                )
                .build();
    }

    private AnalystViewResponse.AnalystViewPartResponse calculateSupplied(List<SuppliedMedicine> list){
        return AnalystViewResponse.AnalystViewPartResponse.builder()
                .amount(
                        list.stream()
                                .map(SuppliedMedicine::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .items(
                        list.stream()
                                .collect(Collectors.groupingBy(it1 -> it1.getMedicine().getName()))
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                                        .map(SuppliedMedicine::getCount)
                                        .reduce(0, Integer::sum)))
                )
                .build();
    }

    //********************** medicine **********************//
    @Transactional
    public AnalystMedicineSalesStatisticsResponse.SalesPerDay medicineSalesPerDay(LocalDate date, Long medicine){

        List<DailyReportMedicine> list =
                dailyReportMedicineRepository.findAll().stream()
                        .filter(l -> Objects.equals(l.getDate(),date) && l.getIsSale())
                        .filter(l-> Objects.equals(l.getMedicine().getId(), medicine))
                        .toList();

        return AnalystMedicineSalesStatisticsResponse.SalesPerDay.builder()
                .date(date)
                .count(list.stream()
                        .map(DailyReportMedicine::getCount)
                        .reduce(0,Integer::sum))
                .amount(list.stream()
                        .map(DailyReportMedicine::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    @Transactional
    public AnalystSalesMedicineResponse salesMedicinesByPeriod(Long medicine, LocalDate start, LocalDate end){
            List<DailyReportMedicine> list =
                    dailyReportMedicineRepository.findAll().stream()
                            .filter(l->
                                    !end.isBefore(l.getDate()) &&
                                            !l.getDate().isBefore(start) &&
                                            l.getIsSale())
                            .filter(l-> Objects.equals(l.getMedicine().getId(), medicine))
                            .toList();

            return AnalystSalesMedicineResponse.builder()
                    .count(list.stream()
                            .map(DailyReportMedicine::getCount)
                            .reduce(0,Integer::sum))
                    .amount(list.stream()
                            .map(DailyReportMedicine::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                    .build();
    }

    @Transactional
    public AnalystSalesMedicineResponse salesMedicinesByMonth(Long medicine, YearMonth date){
        List<MonthReportMedicineSales> reportMedicineSales =
                monthReportMedicineSalesRepository.findAll().stream()
                        .filter(m->Objects.equals(m.getMonth(),date)&&
                                Objects.equals(m.getMedicine().getId(), medicine))
                        .toList();
        if (reportMedicineSales.size()==0)
            return AnalystSalesMedicineResponse.builder()
                    .amount(BigDecimal.ZERO)
                    .count(0)
                    .build();
        return AnalystSalesMedicineResponse.builder()
                .amount(reportMedicineSales.get(0).getAmount())
                .count(reportMedicineSales.get(0).getCount())
                .build();
    }

    @Transactional
    public AnalystStockMedicineResponse stockMedicines(Long medicine, LocalDate date){
        List<MedicineStock> stocks =
                medicineStockRepository.findAll().stream()
                        .filter(m->Objects.equals(m.getDate(), date))
                        .filter(m-> Objects.equals(m.getMedicine().getId(), medicine))
                        .toList();

        return AnalystStockMedicineResponse.builder()
                .stock(
                        AnalystSalesMedicineResponse.builder()
                                .amount(stocks.stream()
                                        .map(MedicineStock::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                                .count(stocks.stream()
                                        .map(MedicineStock::getCount)
                                        .reduce(0, Integer::sum)
                                )
                                .build()
                )
                .build();
    }


    @Transactional
    public AnalystMedicineSalesStatisticsResponse medicineSalesStatistics(LocalDate start, LocalDate end, Long medicine){
        List<AnalystMedicineSalesStatisticsResponse.SalesPerDay> sales = new ArrayList<>();
        while(start.isBefore(end.plusDays(1))){
            sales.add(medicineSalesPerDay(start, medicine));
            start = start.plusDays(1);
        }
        return AnalystMedicineSalesStatisticsResponse.builder()
                .sales(sales)
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
                                    return salesMedicinesByPeriod(id, start, end).getCount();
                                }))
                )
                .build();
    }


    @Transactional
    public AnalystProfitAndLossResponse profitAndLossAnalyseByDate(LocalDate date){
        List<DailyProfitAndLoss> daily  = dailyProfitAndLossRepository.findAll().stream()
                .filter( l->Objects.equals( l.getDate(),date ))
                .toList();

        return AnalystProfitAndLossResponse.builder()
                .profit(daily.stream()
                        .map(DailyProfitAndLoss::getProfit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .loss(daily.stream()
                        .map(DailyProfitAndLoss::getLoss)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }


    @Transactional
    public AnalystProfitAndLossResponse profitAndLossAnalyseByPeriod(LocalDate start, LocalDate end){
        List<DailyProfitAndLoss> daily  = dailyProfitAndLossRepository.findAll().stream()
                .filter( l->!end.isBefore(l.getDate()) && !l.getDate().isBefore(start))
                .toList();

        return AnalystProfitAndLossResponse.builder()
                .profit(daily.stream()
                        .map(DailyProfitAndLoss::getProfit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .loss(daily.stream()
                        .map(DailyProfitAndLoss::getLoss)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }


    @Transactional
    public AnalystProfitAndLossResponse profitAndLossAnalyseByMonth(YearMonth date){
        List<MonthProfitAndLoss> daily  = monthProfitAndLossRepository.findAll().stream()
                .filter( l->Objects.equals(l.getDate(), date ))
                .toList();

        return AnalystProfitAndLossResponse.builder()
                .profit(daily.stream()
                        .map(MonthProfitAndLoss::getProfit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .loss(daily.stream()
                        .map(MonthProfitAndLoss::getLoss)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    @Transactional
    public AnalystAverageCheckResponse averageCheck(LocalDate start, LocalDate end){
        BigDecimal profit = profitAndLossAnalyseByPeriod(start, end).getProfit();
        Integer count = dailyCountSalesRepository.findAll().stream()
                .filter(l-> !end.isBefore(l.getDate()) && !l.getDate().isBefore(start))
                .map(DailyCountSales::getCount)
                .reduce(0, Integer::sum);
        if(count == 0)
            return AnalystAverageCheckResponse.builder()
                    .average(BigDecimal.ZERO)
                    .build();

        return AnalystAverageCheckResponse.builder()
                .average(profit.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP))
                .build();
    }

}
