package ua.knu.pharmacy.dto.response.analyst;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Value
@Builder
public class AnalystMedicineSalesStatisticsResponse {
   List<salesPerDay> sales;

   @Value
   @Builder
   public static class salesPerDay{
      LocalDate date;
      Integer count;
      BigDecimal amount;
   }
}
