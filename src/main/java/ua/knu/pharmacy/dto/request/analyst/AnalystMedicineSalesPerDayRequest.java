package ua.knu.pharmacy.dto.request.analyst;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalystMedicineSalesPerDayRequest {
    private String date;
    private Long medicine;
}
