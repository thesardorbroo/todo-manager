package todo.manager.web.rest;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import todo.manager.security.AuthoritiesConstants;
import todo.manager.service.TodoService;
import todo.manager.service.dto.CustomerResultDTO;
import todo.manager.service.dto.CustomerTasksDTO;
import todo.manager.service.dto.ResponseDTO;
import todo.manager.service.dto.TodoDTO;

@RestController
@RequestMapping("/api")
public class TodoResource {

    private final Logger log = LoggerFactory.getLogger(TodoResource.class);
    private final TodoService todoService;

    public TodoResource(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todo/mark")
    public ResponseEntity<ResponseDTO<TodoDTO>> markTask(@RequestParam Long id) {
        log.debug("REST request to delete Task : {}", id);
        ResponseDTO<TodoDTO> responseDTO = todoService.add(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/todo/tasks")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseDTO<CustomerTasksDTO> getTask() {
        ResponseDTO<CustomerTasksDTO> responseDTO = todoService.tasks();
        return responseDTO;
    }

    @GetMapping("/todo/result/{groupId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseDTO<List<CustomerResultDTO>> getResult(@PathVariable Long groupId) {
        if (groupId == null) {
            return new ResponseDTO<>(false, "ID of Group is null", null);
        }
        ResponseDTO<List<CustomerResultDTO>> responseDTO = todoService.result(groupId);

        return responseDTO;
    }
}
