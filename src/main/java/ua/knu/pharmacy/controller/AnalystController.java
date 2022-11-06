package ua.knu.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.pharmacy.dto.response.analyst.AnalystViewResponse;
import ua.knu.pharmacy.service.AnalystService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analyst")
public class AnalystController {
  private final AnalystService service;

  @GetMapping("/analyse/{request}")
  public AnalystViewResponse analyse(@PathVariable String request) {
    LocalDate date = DateTimeFormatter.ISO_LOCAL_DATE.parse(request, LocalDate::from);
    return service.analyse(date);
  }
}
