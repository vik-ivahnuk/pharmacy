package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {}
