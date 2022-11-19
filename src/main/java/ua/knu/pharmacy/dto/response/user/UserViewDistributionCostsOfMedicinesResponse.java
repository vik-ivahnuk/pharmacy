package ua.knu.pharmacy.dto.response.user;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class UserViewDistributionCostsOfMedicinesResponse {
    BigDecimal total;
    Map<String, BigDecimal> medicines;
}
