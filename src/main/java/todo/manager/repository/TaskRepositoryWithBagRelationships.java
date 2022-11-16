package todo.manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import todo.manager.domain.Task;

public interface TaskRepositoryWithBagRelationships {
    Optional<Task> fetchBagRelationships(Optional<Task> task);

    List<Task> fetchBagRelationships(List<Task> tasks);

    Page<Task> fetchBagRelationships(Page<Task> tasks);
}
