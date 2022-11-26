package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.pharmacy.entity.OrderedMedicines;

public interface OrderedMedicinesRepository extends JpaRepository<OrderedMedicines, Long> {
}
