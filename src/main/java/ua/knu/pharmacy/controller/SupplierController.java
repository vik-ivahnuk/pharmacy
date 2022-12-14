package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateMedicineBatchRequest;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateSupplierRequest;
import ua.knu.pharmacy.dto.response.supplier.SupplierViewMedicineResponse;
import ua.knu.pharmacy.service.SupplierService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier")
public class SupplierController {
  private final SupplierService service;

  @PostMapping("/registration")
  public Long addSupplier(@RequestBody SupplierCreateSupplierRequest request) {
    return service.registration(request);
  }

  @GetMapping("/medicines")
  public List<SupplierViewMedicineResponse> addMedicine() {
    return service.availableMedicines();
  }

  @PostMapping("/supply")
  public Long supply(@RequestBody SupplierCreateMedicineBatchRequest request) {
    return service.supply(request);
  }
}
