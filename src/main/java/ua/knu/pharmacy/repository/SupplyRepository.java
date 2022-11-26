package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.Supply;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {}
