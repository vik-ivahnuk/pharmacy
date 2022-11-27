package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateMedicineBatchItemRequest;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateMedicineBatchRequest;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateSupplierRequest;
import ua.knu.pharmacy.dto.response.supplier.SupplierViewMedicineResponse;
import ua.knu.pharmacy.entity.*;
import ua.knu.pharmacy.exception.NotFoundException;
import ua.knu.pharmacy.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;

    private final SupplyRepository supplyRepository;

    private final SuppliedMedicineRepository suppliedMedicineRepository;

    private final MedicineStockRepository medicineStockRepository;
    private final MonthProfitAndLossRepository monthProfitAndLossRepository;
    private final DailyProfitAndLossRepository dailyProfitAndLossRepository;


    public Long registration(SupplierCreateSupplierRequest request) {
      return supplierRepository
          .save(Supplier.builder().name(request.getName()).creationDate(LocalDate.now()).build())
          .getId();
    }

    public List<SupplierViewMedicineResponse> availableMedicines() {
      return medicineRepository.findAll().stream()
          .map(
              x ->
                  SupplierViewMedicineResponse.builder()
                      .id(x.getId())
                      .name(x.getName())
                      .description(x.getDescription())
                      .price(x.getPrice())
                      .creationDate(x.getCreationDate())
                      .build())
          .toList();
    }

    @Transactional
    public Long supply(SupplierCreateMedicineBatchRequest request) {
        Supplier supplier =
              supplierRepository
                  .findById(request.getSupplierId())
                  .orElseThrow(
                      () -> new NotFoundException("No supplier with id = " + request.getSupplierId()));
        for( SupplierCreateMedicineBatchItemRequest i: request.getItems()){
            if(!LocalDate.now().isBefore(i.getExpirationDate()))
                throw new  NotFoundException("Incorrect expiration date");
            if(!i.getManufactureDate().isBefore( i.getExpirationDate()))
                throw new  NotFoundException("Incorrect manufacture date");
        }
        Supply supply=
            supplyRepository.save(
                    Supply.builder().supplier(supplier).supplyDate(LocalDate.now()).build());
        List<MedicineStock> medicineStockList = new ArrayList<>();

        List<SuppliedMedicine> suppliedMedicines =
                request.getItems().stream()
                        .map(
                            r -> {
                                SuppliedMedicine supp = SuppliedMedicine.builder()
                                        .medicine(medicineRepository.getReferenceById(r.getMedicineId()))
                                        .manufactureDate(r.getManufactureDate())
                                        .expirationDate(r.getExpirationDate())
                                        .supply(supply)
                                        .price(r.getPricePaidSupplier())
                                        .count(r.getCount())
                                        .build();

                                medicineStockList.add(MedicineStock.builder()
                                        .date(LocalDate.now())
                                        .medicine(medicineRepository.getReferenceById(r.getMedicineId()))
                                        .supply(supp)
                                        .count(r.getCount())
                                        .build());
                                return supp;
                            })
                        .toList();

        BigDecimal amount  = BigDecimal.ZERO;
        for(SuppliedMedicine s : suppliedMedicines){
            amount = amount.add(s.getPrice().multiply(
                    BigDecimal.valueOf(s.getCount())
            ));
        }
        suppliedMedicineRepository.saveAll(suppliedMedicines);
        medicineStockRepository.saveAll(medicineStockList);
        updateLoss(LocalDate.now(), amount);
        return supply.getId();
    }

    private void updateLoss(LocalDate date, BigDecimal amount){
        List<DailyProfitAndLoss> daily  = dailyProfitAndLossRepository.findAll().stream()
                .filter(d-> Objects.equals(d.getDate(), date))
                .toList();
        YearMonth yearMonth = YearMonth.from(date);
        List<MonthProfitAndLoss> month = monthProfitAndLossRepository.findAll().stream()
                .filter(mo  -> Objects.equals(mo.getDate(), yearMonth))
                .toList();
        if(daily.size()==0) {
            dailyProfitAndLossRepository.save(
                    DailyProfitAndLoss.builder()
                            .loss(amount)
                            .date(date)
                            .profit(BigDecimal.ZERO)
                            .build()
            );
        } else {
            daily.get(0).setLoss(daily.get(0).getLoss().add(amount));
        }

        if (month.size()==0){
            monthProfitAndLossRepository.save(
                    MonthProfitAndLoss.builder()
                            .loss(amount)
                            .date(yearMonth)
                            .profit(BigDecimal.ZERO)
                            .build()
            );
        } else {
            month.get(0).setLoss(month.get(0).getLoss().add(amount));
        }

    }
}
