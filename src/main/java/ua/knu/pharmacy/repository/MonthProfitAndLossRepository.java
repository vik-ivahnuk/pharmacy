package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.pharmacy.entity.MonthProfitAndLoss;

public interface MonthProfitAndLossRepository extends JpaRepository<MonthProfitAndLoss, Long> {
}
