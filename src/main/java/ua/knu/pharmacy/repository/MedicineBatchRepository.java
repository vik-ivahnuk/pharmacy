package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.MedicineBatch;

@Repository
public interface MedicineBatchRepository extends JpaRepository<MedicineBatch, Long> {}
