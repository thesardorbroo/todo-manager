package todo.manager.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import todo.manager.domain.Groups;

/**
 * Spring Data JPA repository for the Groups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {
    Optional<Groups> findByGroupName(String groupName);

    @Query(value = "DELETE FROM rel_task__groups WHERE groups_id = :groupId", nativeQuery = true)
    void deleteFromRelTask(@Param("groupId") Long groupId);
}
