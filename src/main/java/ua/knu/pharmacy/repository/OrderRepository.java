package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}
