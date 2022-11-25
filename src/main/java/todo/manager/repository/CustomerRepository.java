package todo.manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import todo.manager.domain.Customer;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserLogin(String login);

    @Query("SELECT c FROM Customer c WHERE c.user.login = ?1")
    Optional<Customer> getByUserLogin(String login);

    Optional<List<Customer>> findAllByGroupId(Long groupId);

    Optional<Customer> findByUserId(Long userId);

    Optional<Customer> findByUserIdAndUserLogin(Long userId, String login);

    void deleteByGroupId(Long id);
}
