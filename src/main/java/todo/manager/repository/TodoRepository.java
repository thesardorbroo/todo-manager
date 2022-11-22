package todo.manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import todo.manager.domain.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    @Query(value = "SELECT task_id FROM todo_list WHERE customer_id=:id", nativeQuery = true)
    List<Integer> getTaskIdByCustomerId(@Param("id") Long id);

    Optional<Todo> findByTaskId(Long taskId);
}
