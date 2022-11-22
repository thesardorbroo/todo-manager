package todo.manager.service.impl;

import java.util.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Customer;
import todo.manager.domain.Task;
import todo.manager.domain.Todo;
import todo.manager.repository.TodoRepository;
import todo.manager.service.*;
import todo.manager.service.dto.*;
import todo.manager.service.mapper.CustomerMapper;
import todo.manager.service.mapper.TaskMapper;
import todo.manager.service.mapper.impl.MapperTodoImpl;
import todo.manager.service.validation.GroupValidator;
import todo.manager.web.rest.errors.GroupsExceptions;
import todo.manager.web.rest.errors.TaskException;
import todo.manager.web.rest.errors.TodoExceptions;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    private final TaskService taskService;

    private final CustomerService customerService;

    private final TaskMapper taskMapper;
    private final CustomerMapper customerMapper;
    private final UserService userService;
    private final GroupValidator groupValidator;

    private final GroupsService groupsService;

    public TodoServiceImpl(
        TodoRepository todoRepository,
        TaskService taskService,
        CustomerService customerService,
        TaskMapper taskMapper,
        CustomerMapper customerMapper,
        UserService userService,
        GroupValidator groupValidator,
        GroupsService groupsService
    ) {
        this.todoRepository = todoRepository;
        this.taskService = taskService;
        this.customerService = customerService;
        this.taskMapper = taskMapper;
        this.customerMapper = customerMapper;
        this.userService = userService;
        this.groupValidator = groupValidator;
        this.groupsService = groupsService;
    }

    @Override
    public ResponseDTO<TodoDTO> add(Long taskId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Todo> todoOptional = todoRepository.findByTaskId(taskId);
        if (todoOptional.isPresent()) {
            //            throw TodoExceptions.alreadyExists();
            return null;
        }

        Optional<TaskDTO> taskOptional = taskService.findOne(taskId);
        Optional<CustomerDTO> customerOptional = customerService.findOneByUserLogin(user.getUsername());

        if (taskOptional.isEmpty()) {
            //            throw TaskException.notFound();
            return null;
        }
        Task task = taskOptional.map(taskMapper::toEntity).get();
        Customer customer = customerOptional.map(customerMapper::toEntity).get();

        Todo entity = new Todo(null, customer, task, new Date());

        todoRepository.save(entity);
        return new ResponseDTO<>(true, "OK", MapperTodoImpl.toDTO(entity));
    }

    @Transactional
    @Override
    public ResponseDTO<List<CustomerResultDTO>> result(Long groupId) {
        String searchingResult = findGroup(groupId);
        if (!searchingResult.equals("FOUND")) {
            return new ResponseDTO<>(false, searchingResult, null);
        }
        List<TaskDTO> taskDTOS = taskService.getTasksByGroupId(groupId);
        List<CustomerDTO> customerDTOS = customerService.getCustomerByGroupId(groupId);

        List<CustomerResultDTO> results = new ArrayList<>();

        for (CustomerDTO customerDTO : customerDTOS) {
            List<Integer> taskIds = todoRepository.getTaskIdByCustomerId(customerDTO.getId());

            CustomerResultDTO dto = new CustomerResultDTO();
            dto.setCustomer(customerDTO);
            dto.setDone(taskIds.size());
            dto.setTasksCount(taskDTOS.size());

            if (taskIds.size() != 0 && taskDTOS.size() != 0) {
                dto.setPercent((100 / (taskDTOS.size() / taskIds.size())) * 1.0);
            } else {
                dto.setPercent(0.0);
            }

            List<TaskResultDTO> tasksResult = new ArrayList<>();
            for (TaskDTO t : taskDTOS) {
                boolean value = taskIds.contains(t.getId().intValue());
                tasksResult.add(new TaskResultDTO(t, value));
            }

            dto.setTasks(tasksResult);
            results.add(dto);
        }

        return new ResponseDTO<>(true, "OK", results);
    }

    @Override
    public ResponseDTO<CustomerTasksDTO> tasks() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerDTO customerDTO = customerService.findOneByUserLogin(user.getUsername()).get();

        if (customerDTO.getGroup() == null) {
            return new ResponseDTO<>(false, "You are not attached to some group!", null);
        }

        List<TaskDTO> tasks = taskService.completedTasks(customerDTO.getId());
        List<TaskDTO> taskDTOS = taskService.notCompletedTasks(customerDTO.getId());

        CustomerTasksDTO dto = new CustomerTasksDTO();
        dto.setCompletedTasks(tasks);
        dto.setNotCompleted(taskDTOS);

        ResponseDTO<CustomerTasksDTO> responseDTO = new ResponseDTO<>(true, "OK", dto);

        return responseDTO;
    }

    private String findGroup(Long groupId) {
        if (groupId == null) {
            return "Group id is null!";
        }
        Optional<GroupsDTO> optional = groupsService.findOne(groupId);
        return optional.isPresent() ? "FOUND" : "NOT FOUND";
    }
}
