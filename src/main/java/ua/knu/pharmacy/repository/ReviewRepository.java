package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {}
