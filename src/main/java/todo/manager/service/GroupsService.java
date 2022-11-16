package todo.manager.service;

import java.util.List;
import java.util.Optional;
import todo.manager.service.dto.GroupsDTO;

/**
 * Service Interface for managing {@link todo.manager.domain.Groups}.
 */
public interface GroupsService {
    /**
     * Save a groups.
     *
     * @param groupsDTO the entity to save.
     * @return the persisted entity.
     */
    GroupsDTO save(GroupsDTO groupsDTO);

    /**
     * Updates a groups.
     *
     * @param groupsDTO the entity to update.
     * @return the persisted entity.
     */
    GroupsDTO update(GroupsDTO groupsDTO);

    /**
     * Partially updates a groups.
     *
     * @param groupsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GroupsDTO> partialUpdate(GroupsDTO groupsDTO);

    /**
     * Get all the groups.
     *
     * @return the list of entities.
     */
    List<GroupsDTO> findAll();

    /**
     * Get the "id" groups.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GroupsDTO> findOne(Long id);

    /**
     * Delete the "id" groups.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
