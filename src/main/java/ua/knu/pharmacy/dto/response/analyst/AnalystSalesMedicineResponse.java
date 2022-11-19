package ua.knu.pharmacy.dto.response.analyst;


import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class AnalystSalesMedicineResponse {

    AnalystPartSalesMedicineResponse ordered;
    AnalystPartSalesMedicineResponse expired;

    @Value
    @Builder
    public static class AnalystPartSalesMedicineResponse{
        BigDecimal amount;
        Integer count;
    }
}
