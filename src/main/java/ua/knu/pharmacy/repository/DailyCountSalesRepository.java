package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.DailyCountSales;

@Repository
public interface DailyCountSalesRepository extends JpaRepository<DailyCountSales, Long> {}
