package ua.knu.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.knu.pharmacy.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
