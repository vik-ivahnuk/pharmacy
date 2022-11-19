package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateMedicineBatchItemRequest;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateMedicineBatchRequest;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateSupplierRequest;
import ua.knu.pharmacy.dto.response.supplier.SupplierViewMedicineResponse;
import ua.knu.pharmacy.entity.Medicine;
import ua.knu.pharmacy.entity.MedicineBatch;
import ua.knu.pharmacy.entity.MedicineBundle;
import ua.knu.pharmacy.entity.Supplier;
import ua.knu.pharmacy.exception.NotFoundException;
import ua.knu.pharmacy.repository.MedicineBatchRepository;
import ua.knu.pharmacy.repository.MedicineBundleRepository;
import ua.knu.pharmacy.repository.MedicineRepository;
import ua.knu.pharmacy.repository.SupplierRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;
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
      MedicineBatch batch =
          medicineBatchRepository.save(
              MedicineBatch.builder().supplier(supplier).supplyDate(LocalDate.now()).build());
      Set<Long> requestMedicineIds =
          request.getItems().stream()
              .map(SupplierCreateMedicineBatchItemRequest::getMedicineId)
              .collect(Collectors.toSet());
      Map<Long, Medicine> medicineMap =
          medicineRepository.findAllById(requestMedicineIds).stream()
              .collect(Collectors.toMap(Medicine::getId, Function.identity()));
      List<MedicineBundle> bundles =
          request.getItems().stream()
              .flatMap(
                  it ->
                      IntStream.range(0, it.getCount().intValue())
                          .mapToObj(
                              x ->
                                  MedicineBundle.builder()
                                      .medicine(medicineMap.get(it.getMedicineId()))
                                      .manufactureDate(it.getManufactureDate())
                                      .expirationDate(it.getExpirationDate())
                                      .pricePaidSupplier(it.getPricePaidSupplier())
                                      .medicineBatch(batch)
                                      .build()))
              .toList();
      medicineBundleRepository.saveAll(bundles);
      return batch.getId();
    }
}
