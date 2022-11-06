package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.MedicineBundle;

@Repository
public interface MedicineBundleRepository extends JpaRepository<MedicineBundle, Long> {}
