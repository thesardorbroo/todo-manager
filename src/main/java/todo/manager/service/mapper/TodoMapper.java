package todo.manager.service.mapper;

import org.mapstruct.*;
import todo.manager.domain.Customer;
import todo.manager.domain.Task;
import todo.manager.domain.Todo;
import todo.manager.service.dto.CustomerDTO;
import todo.manager.service.dto.TaskDTO;
import todo.manager.service.dto.TodoDTO;

/**
 * Mapper for the entity {@link Todo} and its DTO {@link TodoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TodoMapper extends EntityMapper<TodoDTO, Todo> {
    @Mapping(target = "task", source = "task", qualifiedByName = "taskId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    TodoDTO toDto(Todo s);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
