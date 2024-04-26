package karpiuk.karpiuk_test_task.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import karpiuk.karpiuk_test_task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    List<User> findAllByBirthDateBetween(LocalDate from, LocalDate to);
}
