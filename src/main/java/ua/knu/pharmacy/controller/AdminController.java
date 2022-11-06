package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.pharmacy.dto.request.admin.AdminAddMedicineRequest;
import ua.knu.pharmacy.dto.request.supplier.SupplierCreateSupplierRequest;
import ua.knu.pharmacy.service.AdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
  private final AdminService service;

  @PostMapping("/medicines")
  public Long addMedicine(@RequestBody AdminAddMedicineRequest request) {
    return service.addMedicine(request);
  }

  @PostMapping("/suppliers")
  public Long addSupplier(@RequestBody SupplierCreateSupplierRequest request) {
    return service.addSupplier(request);
  }
}
