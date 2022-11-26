package ua.knu.pharmacy.dto.request.analyst;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalystProfitAndLossSupplierRequest {
    String start;
    String end;
    Long supplier;
}
