package todo.manager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import todo.manager.repository.TodoRepository;
import todo.manager.service.TodoService;
import todo.manager.service.dto.CustomerResultDTO;
import todo.manager.service.dto.ResponseDTO;
import todo.manager.service.dto.TodoDTO;
import todo.manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link todo.manager.domain.Todo}.
 */
@RestController
@RequestMapping("/api")
public class TodoResource {

    private final Logger log = LoggerFactory.getLogger(TodoResource.class);

    private static final String ENTITY_NAME = "todo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TodoService todoService;

    private final TodoRepository todoRepository;

    public TodoResource(TodoService todoService, TodoRepository todoRepository) {
        this.todoService = todoService;
        this.todoRepository = todoRepository;
    }

    /**
     * {@code POST  /todos} : Create a new todo.
     *
     * @param todoDTO the todoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new todoDTO, or with status {@code 400 (Bad Request)} if the todo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/todos")
    public ResponseEntity<TodoDTO> createTodo(@RequestBody TodoDTO todoDTO) throws URISyntaxException {
        log.debug("REST request to save Todo : {}", todoDTO);
        if (todoDTO.getId() != null) {
            throw new BadRequestAlertException("A new todo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TodoDTO result = todoService.save(todoDTO);
        return ResponseEntity
            .created(new URI("/api/todos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /todos/:id} : Updates an existing todo.
     *
     * @param id the id of the todoDTO to save.
     * @param todoDTO the todoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todoDTO,
     * or with status {@code 400 (Bad Request)} if the todoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the todoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable(value = "id", required = false) final Long id, @RequestBody TodoDTO todoDTO)
        throws URISyntaxException {
        log.debug("REST request to update Todo : {}, {}", id, todoDTO);
        if (todoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TodoDTO result = todoService.update(todoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /todos/:id} : Partial updates given fields of an existing todo, field will ignore if it is null
     *
     * @param id the id of the todoDTO to save.
     * @param todoDTO the todoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todoDTO,
     * or with status {@code 400 (Bad Request)} if the todoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the todoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the todoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/todos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TodoDTO> partialUpdateTodo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TodoDTO todoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Todo partially : {}, {}", id, todoDTO);
        if (todoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TodoDTO> result = todoService.partialUpdate(todoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /todos} : get all the todos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of todos in body.
     */
    @GetMapping("/todos")
    public List<TodoDTO> getAllTodos() {
        log.debug("REST request to get all Todos");
        return todoService.findAll();
    }

    /**
     * {@code GET  /todos/:id} : get the "id" todo.
     *
     * @param id the id of the todoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the todoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoDTO> getTodo(@PathVariable Long id) {
        log.debug("REST request to get Todo : {}", id);
        Optional<TodoDTO> todoDTO = todoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(todoDTO);
    }

    @GetMapping("/todo/result/{groupId}")
    public ResponseDTO<List<CustomerResultDTO>> getResultsOnPercent(@PathVariable(required = false) Long groupId) {
        if (groupId == null) {
            return new ResponseDTO<>(false, "Path variable \"Group ID\" is null!", null);
        }
        return todoService.getResultsOnPercent(groupId);
    }

    /**
     * {@code DELETE  /todos/:id} : delete the "id" todo.
     *
     * @param id the id of the todoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        log.debug("REST request to delete Todo : {}", id);
        todoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/todo/mark/{taskId}")
    public ResponseDTO<TodoDTO> markTask(@PathVariable(required = false) Long taskId) {
        if (taskId == null) {
            return new ResponseDTO<>(true, "Path variable \"Task ID\" is null!", null);
        }
        return todoService.mark(taskId);
    }
}
