package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.pharmacy.entity.DailyCountSales;
import ua.knu.pharmacy.entity.DailyProfitAndLoss;

public interface DailyProfitAndLossRepository extends JpaRepository<DailyProfitAndLoss, Long> { }
