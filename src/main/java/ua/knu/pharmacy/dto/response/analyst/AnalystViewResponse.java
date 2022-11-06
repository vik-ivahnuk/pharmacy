package ua.knu.pharmacy.dto.response.analyst;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class AnalystViewResponse {
  AnalystViewPartResponse supplied;
  AnalystViewPartResponse ordered;
  AnalystViewPartResponse expired;

  @Value
  @Builder
  public static class AnalystViewPartResponse {
    BigDecimal amount;
    Map<String, Integer> items;
  }
}
