package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.dto.response.analyst.AnalystViewResponse;
import ua.knu.pharmacy.entity.MedicineBundle;
import ua.knu.pharmacy.repository.MedicineBatchRepository;
import ua.knu.pharmacy.repository.MedicineBundleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalystService {
  private final MedicineBundleRepository medicineBundleRepository;
  private final MedicineBatchRepository medicineBatchRepository;

  @Transactional
  public AnalystViewResponse analyse(LocalDate date) {
    List<MedicineBundle> supplied =
        medicineBatchRepository.findAll().stream()
            .filter(batch -> batch.getSupplyDate().isEqual(date))
            .flatMap(batch -> batch.getBundles().stream())
            .toList();
    List<MedicineBundle> ordered =
        medicineBundleRepository.findAll().stream()
            .filter(bundle -> bundle.getOrder() != null)
            .filter(bundle -> bundle.getOrder().getDate().isEqual(date))
            .toList();
    List<MedicineBundle> expired =
        medicineBundleRepository.findAll().stream()
            .filter(bundle -> bundle.getOrder() == null)
            .filter(bundle -> bundle.getExpirationDate().isEqual(date))
            .toList();
    return AnalystViewResponse.builder()
        .supplied(analyse(supplied))
        .ordered(analyse(ordered))
        .expired(analyse(expired))
        .build();
  }

  private AnalystViewResponse.AnalystViewPartResponse analyse(List<MedicineBundle> data) {

    return AnalystViewResponse.AnalystViewPartResponse.builder()
        .amount(
            data.stream()
                .map(x -> x.getMedicine().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add))
        .items(
            data.stream()
                .collect(Collectors.groupingBy(it1 -> it1.getMedicine().getName()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size())))
        .build();
  }
}
