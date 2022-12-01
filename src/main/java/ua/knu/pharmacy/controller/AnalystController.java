package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.knu.pharmacy.dto.request.DateRequest;
import ua.knu.pharmacy.dto.request.TimeRequest;
import ua.knu.pharmacy.dto.request.analyst.*;
import ua.knu.pharmacy.dto.response.analyst.*;
import ua.knu.pharmacy.service.AnalystService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analyst")
public class AnalystController {
  private final AnalystService service;

  @GetMapping("/analyse/supply/stocks")
  public AnalystStocksSupplyResponse analyseStocksOfButch(@RequestBody AnalystSupplyDateRequest request) {
    LocalDate date = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getDate(), LocalDate::from);
    return service.stocksSupply(request.getSupply(), date);
  }

  @GetMapping("/analyse/supply/sales/date")
  public AnalystViewResponse analyseSalesOfSupplyByDate(@RequestBody AnalystSupplyDateRequest request) {
    LocalDate date = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getDate(), LocalDate::from);
    return service.salesOfSupplyByDay(request.getSupply(), date);
  }

  @GetMapping("/analyse/supply/sales/period")
  public AnalystViewResponse analyseSalesOfSupplyByPeriod(@RequestBody AnalystSupplyPeriodRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.salesOfSupplyByPeriod(request.getSupply(), start, end);
  }

  @GetMapping("/analyse/supply/sales/month")
  public AnalystViewResponse analyseSalesOfSupplyByMonth(@RequestBody AnalystSupplyDateRequest request) {
    YearMonth date = YearMonth.parse(request.getDate());
    return service.salesOfSupplyByMonth(request.getSupply(), date);
  }



  @GetMapping("/analyse/medicine/sales/per day/")
  public AnalystMedicineSalesStatisticsResponse.SalesPerDay analyseSalesMedicinePerDay(@RequestBody AnalystMedicineDateRequest request) {
    LocalDate date = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getDate(), LocalDate::from);
    return service.medicineSalesPerDay(date, request.getMedicine());
  }

  @GetMapping("/analyse/medicine/sales/period")
  public AnalystSalesMedicineResponse analyseSalesMedicineByPeriod(@RequestBody AnalystMedicinePeriodRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.salesMedicinesByPeriod(request.getMedicine(), start, end);
  }

  @GetMapping("/analyse/medicine/sales/month/")
  public AnalystSalesMedicineResponse analyseSalesMedicinePerMonth(@RequestBody AnalystMedicineDateRequest request) {
    YearMonth date = YearMonth.parse(request.getDate());
    return service.salesMedicinesByMonth( request.getMedicine(), date);
  }

  @GetMapping("/analyse/medicine/stock/")
  public AnalystStockMedicineResponse analyseStockMedicinePerDay(@RequestBody AnalystMedicineDateRequest request) {
    LocalDate date = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getDate(), LocalDate::from);
    return service.stockMedicines(request.getMedicine(), date);
  }

  @GetMapping("/analyse/medicine/sales/statistics/")
  public AnalystMedicineSalesStatisticsResponse analyseSalesMedicineByTime(@RequestBody AnalystMedicinePeriodRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.medicineSalesStatistics(start, end, request.getMedicine());
  }

  @GetMapping("/analyse/medicine/distribution sales/")
  public AnalystDistributionSalesMedicineResponse analyseDistributionSalesMedicine(@RequestBody TimeRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.DistributionSalesMedicine(start, end);
  }



  @GetMapping("/analyse/profit and loss/date/")
  public AnalystProfitAndLossResponse analyseProfitAndLossByPeriod(@RequestBody DateRequest request) {
    LocalDate date = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getDate(), LocalDate::from);
    return service.profitAndLossAnalyseByDate(date);
  }

  @GetMapping("/analyse/profit and loss/period/")
  public AnalystProfitAndLossResponse analyseProfitAndLossByDate(@RequestBody TimeRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.profitAndLossAnalyseByPeriod(start, end);
  }

  @GetMapping("/analyse/profit and loss/month/")
  public AnalystProfitAndLossResponse analyseProfitAndLossByMonth(@RequestBody DateRequest request) {
    YearMonth date = YearMonth.parse(request.getDate());
    return service.profitAndLossAnalyseByMonth(date);
  }




  @GetMapping("/analyse/average check/")
  public AnalystAverageCheckResponse analyseAverageCheck(@RequestBody TimeRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.averageCheck(start, end);
  }
}
