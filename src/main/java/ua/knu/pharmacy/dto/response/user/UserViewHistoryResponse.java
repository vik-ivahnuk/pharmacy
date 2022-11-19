package ua.knu.pharmacy.dto.response.user;


import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class UserViewHistoryResponse {

    BigDecimal total;
    List<OrderMedicines> orders;

    @Value
    @Builder
    public static class OrderMedicines {
        LocalDate date;
        BigDecimal amount;
        List<OrderedMedicine> medicines;

        @Value
        @Builder
        public static class OrderedMedicine{
            String name;
            Integer count;
            BigDecimal price;
        }
    }
}
