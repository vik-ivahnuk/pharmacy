package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.knu.pharmacy.dto.request.TimeRequest;
import ua.knu.pharmacy.dto.request.analyst.AnalystButchRequest;
import ua.knu.pharmacy.dto.request.analyst.AnalystMedicineSalesPerDayRequest;
import ua.knu.pharmacy.dto.request.analyst.AnalystMedicineSalesStatisticsRequest;
import ua.knu.pharmacy.dto.request.analyst.AnalystSalesMedicineRequest;
import ua.knu.pharmacy.dto.response.analyst.*;
import ua.knu.pharmacy.service.AnalystService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analyst")
public class AnalystController {
  private final AnalystService service;

  @GetMapping("/analyse/analyse per day/{request}")
  public AnalystViewResponse analysePerDay(@PathVariable String request) {
    LocalDate date = DateTimeFormatter.ISO_LOCAL_DATE.parse(request, LocalDate::from);
    return service.analysePerDay(date);
  }

  @GetMapping("/analyse/butch/sales")
  public AnalystViewResponse analyseSalesOfButch(@RequestBody AnalystButchRequest request) {
    return service.salesOfButch(request.getButch());
  }

  @GetMapping("/analyse/butch/stocks")
  public AnalystStocksButchResponse analyseStocksOfButch(@RequestBody AnalystButchRequest request) {
    return service.stocksButch(request.getButch());
  }

  @GetMapping("/analyse/medicine/sales per day/")
  public AnalystMedicineSalesStatisticsResponse.salesPerDay analyseSalesMedicinePerDay(@RequestBody AnalystMedicineSalesPerDayRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getDate(), LocalDate::from);
    return service.medicineSalesPerDay(start, request.getMedicine());
  }

  @GetMapping("/analyse/medicine/sales/")
  public AnalystSalesMedicineResponse analyseSalesMedicinePerDay(@RequestBody AnalystMedicineSalesStatisticsRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.salesMedicines(request.getMedicine(), start, end);
  }

  @GetMapping("/analyse/medicine/stock/")
  public AnalystStockMedicineResponse analyseStockMedicinePerDay(@RequestBody AnalystSalesMedicineRequest request) {
    return service.stockMedicines(request.getMedicine());
  }

  @GetMapping("/analyse/medicine/sales statistics/")
  public AnalystMedicineSalesStatisticsResponse analyseSalesMedicineByTime(@RequestBody AnalystMedicineSalesStatisticsRequest request) {
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

  @GetMapping("/analyse/medicine/profit and loss/")
  public AnalystProfitAndLossResponse analyseProfitAndLoss(@RequestBody TimeRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.profitAndLossAnalyse(start, end);
  }

  @GetMapping("/analyse/average check/")
  public AnalystAverageCheckResponse analyseAverageCheck(@RequestBody TimeRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.averageCheck(start, end);
  }
}
