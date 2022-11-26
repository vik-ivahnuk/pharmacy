package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.pharmacy.entity.MedicineStock;

public interface MedicineStockRepository extends JpaRepository<MedicineStock, Long> {
}
