package todo.manager.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import todo.manager.domain.Task;
import todo.manager.domain.Todo;

/**
 * Spring Data JPA repository for the Todo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT t.task.id FROM Todo t WHERE t.customer.id = :customerId")
    Set<Long> findIdByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT t.task FROM Todo t WHERE t.customer.id = :customerId")
    Set<Task> findTaskByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT t.id FROM Todo t WHERE t.task.id = :taskId AND t.customer.id = :customerId")
    Long isTaskMarked(@Param("taskId") Long taskId, @Param("customerId") Long customerId);
}
