package todo.manager.service.mapper.impl;

import todo.manager.domain.Customer;
import todo.manager.domain.Todo;
import todo.manager.service.dto.CustomerDTO;
import todo.manager.service.dto.TodoDTO;

public class MapperTodoImpl {

    public static TodoDTO toDTO(Todo todo) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(todo.getCustomer().getId());

        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTask(MapperTaskImpl.toDtoWithoutGroups(todo.getTask()));
        dto.setCustomer(customerDTO);
        dto.setCreatedAt(todo.getCreatedAt());

        return dto;
    }
}
