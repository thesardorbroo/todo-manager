package todo.manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import todo.manager.domain.Groups;

/**
 * Spring Data JPA repository for the Groups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {}
