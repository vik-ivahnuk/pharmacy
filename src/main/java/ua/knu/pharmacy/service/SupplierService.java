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

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;

    private final SupplyRepository supplyRepository;

    private final SuppliedMedicineRepository suppliedMedicineRepository;

    private final MedicineStockRepository medicineStockRepository;

    private final MedicineBatchRepository medicineBatchRepository;

    private final MedicineBundleRepository medicineBundleRepository;

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
        Supply supply=
            supplyRepository.save(
                    Supply.builder().supplier(supplier).supplyDate(LocalDate.now()).build());

        List<SuppliedMedicine> suppliedMedicines =
                request.getItems().stream()
                        .map(
                            r ->
                                 SuppliedMedicine.builder()
                                     .medicine(medicineRepository.getReferenceById(r.getMedicineId()))
                                     .manufactureDate(r.getManufactureDate())
                                     .expirationDate(r.getExpirationDate())
                                     .supply(supply)
                                     .price(r.getPricePaidSupplier())
                                     .count(r.getCount())
                                     .build())
                        .toList();

        List<MedicineStock> medicineStockList =
                request.getItems().stream()
                        .map(
                                r ->
                                        MedicineStock.builder()
                                                .date(LocalDate.now())
                                                .medicine(medicineRepository.getReferenceById(r.getMedicineId()))
                                                .supply(supply)
                                                .count(r.getCount())
                                                .build())
                        .toList();
        medicineStockRepository.saveAll(medicineStockList);
        suppliedMedicineRepository.saveAll(suppliedMedicines);

        return supply.getId();
    }
}
