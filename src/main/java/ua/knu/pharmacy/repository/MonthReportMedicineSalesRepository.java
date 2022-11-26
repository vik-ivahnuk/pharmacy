package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.pharmacy.entity.MonthReportMedicineSales;

public interface MonthReportMedicineSalesRepository extends JpaRepository<MonthReportMedicineSales, Long> {
}
