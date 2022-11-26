package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.SuppliedMedicine;


@Repository
public interface SuppliedMedicineRepository extends JpaRepository<SuppliedMedicine, Long> {
}
