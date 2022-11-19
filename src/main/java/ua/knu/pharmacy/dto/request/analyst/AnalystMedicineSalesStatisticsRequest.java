package ua.knu.pharmacy.dto.request.analyst;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalystMedicineSalesStatisticsRequest {
    private String start;
    private String end;
    private Long medicine;
}
