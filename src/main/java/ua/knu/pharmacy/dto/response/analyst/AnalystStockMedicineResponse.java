package ua.knu.pharmacy.dto.response.analyst;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnalystStockMedicineResponse {
    AnalystSalesMedicineResponse stock;
}
