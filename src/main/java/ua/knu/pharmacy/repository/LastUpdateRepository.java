package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.pharmacy.entity.LastUpdate;

public interface LastUpdateRepository extends JpaRepository<LastUpdate, Long> {
}
