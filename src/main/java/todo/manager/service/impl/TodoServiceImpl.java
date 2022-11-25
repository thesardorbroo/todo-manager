package todo.manager.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Task;
import todo.manager.domain.Todo;
import todo.manager.repository.TaskRepository;
import todo.manager.repository.TodoRepository;
import todo.manager.service.CustomerService;
import todo.manager.service.GroupsService;
import todo.manager.service.TaskService;
import todo.manager.service.TodoService;
import todo.manager.service.dto.*;
import todo.manager.service.mapper.TodoMapper;
import todo.manager.service.mapper.impl.MapperTaskImpl;

/**
 * Service Implementation for managing {@link Todo}.
 */
@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    private final Logger log = LoggerFactory.getLogger(TodoServiceImpl.class);

    private final TodoRepository todoRepository;

    private final TodoMapper todoMapper;

    private final CustomerService customerService;

    private final GroupsService groupsService;

    private final TaskRepository taskRepository;

    public TodoServiceImpl(
        TodoRepository todoRepository,
        TodoMapper todoMapper,
        CustomerService customerService,
        GroupsService groupsService,
        TaskRepository taskRepository
    ) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        this.customerService = customerService;
        this.groupsService = groupsService;
        this.taskRepository = taskRepository;
    }

    @Override
    public TodoDTO save(TodoDTO todoDTO) {
        log.debug("Request to save Todo : {}", todoDTO);
        Todo todo = todoMapper.toEntity(todoDTO);
        todo = todoRepository.save(todo);
        return todoMapper.toDto(todo);
    }

    @Override
    public TodoDTO update(TodoDTO todoDTO) {
        log.debug("Request to update Todo : {}", todoDTO);
        Todo todo = todoMapper.toEntity(todoDTO);
        todo = todoRepository.save(todo);
        return todoMapper.toDto(todo);
    }

    @Override
    public Optional<TodoDTO> partialUpdate(TodoDTO todoDTO) {
        log.debug("Request to partially update Todo : {}", todoDTO);

        return todoRepository
            .findById(todoDTO.getId())
            .map(existingTodo -> {
                todoMapper.partialUpdate(existingTodo, todoDTO);

                return existingTodo;
            })
            .map(todoRepository::save)
            .map(todoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoDTO> findAll() {
        log.debug("Request to get all Todos");
        return todoRepository.findAll().stream().map(todoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TodoDTO> findOne(Long id) {
        log.debug("Request to get Todo : {}", id);
        return todoRepository.findById(id).map(todoMapper::toDto);
    }

    @Override
    public ResponseDTO<List<CustomerResultDTO>> getResultsOnPercent(Long groupId) {
        Optional<GroupsDTO> groupsOptional = groupsService.findOne(groupId);
        if (groupsOptional.isEmpty()) {
            return new ResponseDTO<>(false, "Tasks are not found, ID of group is invalid!", null);
        }
        GroupsDTO group = groupsOptional.get();
        List<CustomerDTO> customers = customerService.getCustomerByGroupId(groupId);
        List<CustomerResultDTO> customerResults = new ArrayList<>();
        for (CustomerDTO customer : customers) {
            Set<TaskDTO> tasks = group.getTasks();
            Set<Long> customerTasksIds = todoRepository.findIdByCustomerId(customer.getId());

            int count = 0;
            List<TaskResultDTO> taskResults = new ArrayList<>();
            for (TaskDTO task : tasks) {
                boolean isDone = customerTasksIds.contains(task.getId());
                count += isDone ? 1 : 0;
                taskResults.add(new TaskResultDTO(task, isDone));
            }

            CustomerResultDTO customerResult = new CustomerResultDTO();
            customerResult.setCustomer(customer);
            customerResult.setTasksCount(tasks.size());
            customerResult.setTasks(taskResults);
            customerResult.setDone(count);
            //            customerResult.setPercent((100 / (tasks.size() - count)));
            customerResults.add(customerResult);
        }

        return new ResponseDTO<>(true, "OK", customerResults);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Todo : {}", id);
        todoRepository.deleteById(id);
    }

    @Override
    public ResponseDTO<TodoDTO> mark(Long taskId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CustomerDTO> customerOptional = customerService.findOneByUserLogin(user.getUsername());
        if (customerOptional.isEmpty()) {
            return new ResponseDTO<>(false, "User is not found!", null);
        }
        CustomerDTO customer = customerOptional.get();
        Long isTaskMarked = todoRepository.isTaskMarked(taskId, customer.getId());
        if (isTaskMarked != null) {
            return new ResponseDTO<>(false, "Task is already marked!", null);
        }

        TaskDTO customerTask = null;
        //        Set<TaskDTO> notMarkedTasks =
        //            todoRepository.findTaskByCustomerId(customer.getId())
        //                .stream().map(MapperTaskImpl::toDtoWithoutGroups).collect(Collectors.toSet());
        for (TaskDTO task : customer.getGroup().getTasks()) {
            if (task.getId().equals(taskId)) {
                customerTask = task;
                break;
            }
        }
        if (customerTask == null) {
            return new ResponseDTO<>(false, "\"Task ID\" is invalid!", null);
        }
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTask(customerTask);
        todoDTO.setCustomer(customer);
        todoDTO.setNone(true);
        todoDTO.setCreatedAt(LocalDate.now());

        Todo entity = todoMapper.toEntity(todoDTO);
        todoRepository.save(entity);
        return new ResponseDTO<>(true, "OK", todoMapper.toDto(entity));
    }
}
