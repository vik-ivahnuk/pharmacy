package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.pharmacy.dto.request.admin.AdminAddMedicineRequest;
import ua.knu.pharmacy.service.AdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
  private final AdminService adminService;

  @PostMapping("/medicine")
  public void addMedicine(@RequestBody AdminAddMedicineRequest request) {
    adminService.addMedicine(request);
  }
}
