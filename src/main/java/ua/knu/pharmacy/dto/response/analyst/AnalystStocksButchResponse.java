package ua.knu.pharmacy.dto.response.analyst;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnalystStocksButchResponse {
    AnalystViewResponse.AnalystViewPartResponse stocks;
}
