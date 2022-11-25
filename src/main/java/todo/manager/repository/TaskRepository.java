package todo.manager.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.concurrent.ScheduledExecutorTask;
import org.springframework.stereotype.Repository;
import todo.manager.domain.Task;

/**
 * Spring Data JPA repository for the Task entity.
 *
 * When extending this class, extend TaskRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TaskRepository extends TaskRepositoryWithBagRelationships, JpaRepository<Task, Long> {
    @Query(
        value = "SELECT * FROM task t WHERE t.id in (SELECT r.task_id FROM rel_task__groups r WHERE groups_id = :groupId )",
        nativeQuery = true
    )
    List<Task> findAllByGroups(@Param("groupId") Long groupId);

    @Query(value = "SELECT t FROM Task t WHERE t.id IN (SELECT td.task.id FROM Todo td WHERE td.customer.id = :customerId)")
    List<Task> getCompletedTasks(@Param("customerId") Long customerId);

    @Query(value = "SELECT t FROM Task t WHERE t.id NOT IN (SELECT td.task.id FROM Todo td WHERE td.customer.id = :customerId)")
    List<Task> getNotCompletedTasks(@Param("customerId") Long customerId);

    @Query("SELECT t FROM Task t WHERE t.id NOT IN (SELECT td.task.id FROM Todo td WHERE td.customer.id = :customerId)")
    Set<Task> getNotMarkedTasks(@Param("customerId") Long customerId);

    void deleteById(Long id);

    default Optional<Task> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Task> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Task> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
