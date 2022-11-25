package todo.manager.service;

import java.util.List;
import java.util.Optional;
import todo.manager.service.dto.CustomerResultDTO;
import todo.manager.service.dto.ResponseDTO;
import todo.manager.service.dto.TodoDTO;

/**
 * Service Interface for managing {@link todo.manager.domain.Todo}.
 */
public interface TodoService {
    /**
     * Save a todo.
     *
     * @param todoDTO the entity to save.
     * @return the persisted entity.
     */
    TodoDTO save(TodoDTO todoDTO);

    /**
     * Updates a todo.
     *
     * @param todoDTO the entity to update.
     * @return the persisted entity.
     */
    TodoDTO update(TodoDTO todoDTO);

    /**
     * Partially updates a todo.
     *
     * @param todoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TodoDTO> partialUpdate(TodoDTO todoDTO);

    /**
     * Get all the todos.
     *
     * @return the list of entities.
     */
    List<TodoDTO> findAll();

    /**
     * Get the "id" todo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TodoDTO> findOne(Long id);

    ResponseDTO<List<CustomerResultDTO>> getResultsOnPercent(Long groupId);

    /**
     * Delete the "id" todo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    ResponseDTO<TodoDTO> mark(Long taskId);
}
