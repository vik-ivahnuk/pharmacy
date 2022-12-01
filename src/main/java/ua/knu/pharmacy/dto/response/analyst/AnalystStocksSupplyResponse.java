package ua.knu.pharmacy.dto.response.analyst;


import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AnalystStocksSupplyResponse {

    Map<String, Integer> stock;
}
