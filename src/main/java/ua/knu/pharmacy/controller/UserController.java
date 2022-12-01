package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.knu.pharmacy.dto.request.user.UserCreateUserRequest;
import ua.knu.pharmacy.dto.request.user.UserOrderRequest;
import ua.knu.pharmacy.dto.request.user.UserReviewRequest;
import ua.knu.pharmacy.dto.request.user.UserViewHistoryRequest;
import ua.knu.pharmacy.dto.response.user.UserViewHistoryResponse;
import ua.knu.pharmacy.dto.response.user.UserViewProductResponse;
import ua.knu.pharmacy.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
  public Boolean order(@RequestBody UserOrderRequest request) {
    return service.order(request);
  }

  @PostMapping("/medicines/review")
  public Long review(@RequestBody UserReviewRequest request) {
    return service.review(request);
  }

  @GetMapping("/statistics/history")
  public UserViewHistoryResponse showHistory(@RequestBody UserViewHistoryRequest request) {
    LocalDate start = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getStart(), LocalDate::from);
    LocalDate end = DateTimeFormatter.ISO_LOCAL_DATE.parse(request.getEnd(), LocalDate::from);
    return service.showHistory(start, end, request.getUserId());
  }

}
