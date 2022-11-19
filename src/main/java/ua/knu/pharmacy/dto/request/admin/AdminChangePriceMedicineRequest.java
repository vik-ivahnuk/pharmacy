package ua.knu.pharmacy.dto.request.admin;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdminChangePriceMedicineRequest {
    private Long medicine;
    private BigDecimal price;
}
