package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.pharmacy.dto.response.user.UserViewProductResponse;
import ua.knu.pharmacy.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService service;

  @GetMapping("/medicines")
  public List<UserViewProductResponse> getAvailableProducts() {
    return service.getAvailableProducts();
  }
}
