package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.pharmacy.dto.request.user.UserCreateUserRequest;
import ua.knu.pharmacy.dto.request.user.UserOrderRequest;
import ua.knu.pharmacy.dto.response.user.UserViewProductResponse;
import ua.knu.pharmacy.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService service;

  @PostMapping("/registration")
  public Long addSupplier(@RequestBody UserCreateUserRequest request) {
    return service.registration(request);
  }

  @GetMapping("/medicines")
  public List<UserViewProductResponse> getAvailableProducts() {
    return service.getAvailableProducts();
  }

  @PostMapping("/medicines/order")
  public void order(@RequestBody List<UserOrderRequest> requests) {
    service.order(requests);
  }
}
