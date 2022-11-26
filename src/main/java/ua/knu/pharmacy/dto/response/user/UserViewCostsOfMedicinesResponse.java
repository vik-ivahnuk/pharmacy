package ua.knu.pharmacy.dto.response.user;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class UserViewCostsOfMedicinesResponse {
    BigDecimal total;
    Map<String, BigDecimal> medicines;
}
