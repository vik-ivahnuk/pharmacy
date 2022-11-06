package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.pharmacy.dto.response.supplier.SupplierViewMedicineResponse;
import ua.knu.pharmacy.service.SupplierService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier")
public class SupplierController {
  private final SupplierService service;

  @GetMapping("/medicine")
  public List<SupplierViewMedicineResponse> addMedicine() {
    return service.availableMedicines();
  }
}
