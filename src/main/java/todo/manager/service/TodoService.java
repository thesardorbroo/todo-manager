package todo.manager.service;

import java.util.List;
import java.util.Optional;
import todo.manager.service.dto.*;

public interface TodoService {
    ResponseDTO<TodoDTO> add(Long taskDTO);

    ResponseDTO<List<CustomerResultDTO>> result(Long groupId);

    ResponseDTO<CustomerTasksDTO> tasks();
}
