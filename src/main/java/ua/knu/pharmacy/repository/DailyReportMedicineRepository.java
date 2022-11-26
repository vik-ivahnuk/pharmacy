package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.pharmacy.entity.DailyProfitAndLoss;
import ua.knu.pharmacy.entity.DailyReportMedicine;

public interface DailyReportMedicineRepository extends JpaRepository<DailyReportMedicine, Long> { }
