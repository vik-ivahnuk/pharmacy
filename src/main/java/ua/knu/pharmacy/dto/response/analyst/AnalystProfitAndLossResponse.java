package ua.knu.pharmacy.dto.response.analyst;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class AnalystProfitAndLossResponse {
    BigDecimal profit;
    BigDecimal loss;
}
