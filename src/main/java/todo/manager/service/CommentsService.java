package todo.manager.service;

import java.util.List;
import java.util.Optional;
import todo.manager.service.dto.CommentsDTO;

/**
 * Service Interface for managing {@link todo.manager.domain.Comments}.
 */
public interface CommentsService {
    /**
     * Save a comments.
     *
     * @param commentsDTO the entity to save.
     * @return the persisted entity.
     */
    CommentsDTO save(CommentsDTO commentsDTO);

    /**
     * Updates a comments.
     *
     * @param commentsDTO the entity to update.
     * @return the persisted entity.
     */
    CommentsDTO update(CommentsDTO commentsDTO);

    /**
     * Partially updates a comments.
     *
     * @param commentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentsDTO> partialUpdate(CommentsDTO commentsDTO);

    /**
     * Get all the comments.
     *
     * @return the list of entities.
     */
    List<CommentsDTO> findAll();

    /**
     * Get the "id" comments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentsDTO> findOne(Long id);

    /**
     * Delete the "id" comments.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
