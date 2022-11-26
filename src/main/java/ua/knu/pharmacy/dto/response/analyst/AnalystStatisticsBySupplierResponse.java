package ua.knu.pharmacy.dto.response.analyst;


import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AnalystStatisticsBySupplierResponse {
    Map<String, AnalystProfitAndLossResponse> profitAndLoss;
}
