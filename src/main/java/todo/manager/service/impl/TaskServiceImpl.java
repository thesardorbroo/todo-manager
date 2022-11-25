package todo.manager.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Task;
import todo.manager.repository.TaskRepository;
import todo.manager.service.CustomerService;
import todo.manager.service.TaskService;
import todo.manager.service.dto.*;
import todo.manager.service.mapper.TaskMapper;
import todo.manager.service.mapper.impl.MapperTaskImpl;
import todo.manager.service.validation.GroupValidator;

/**
 * Service Implementation for managing {@link Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final GroupValidator groupValidator;

    private final CustomerService customerService;

    public TaskServiceImpl(
        TaskRepository taskRepository,
        TaskMapper taskMapper,
        GroupValidator groupValidator,
        CustomerService customerService
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.groupValidator = groupValidator;
        this.customerService = customerService;
    }

    @Override
    public ResponseDTO<TaskDTO> save(TaskDTO taskDTO) {
        log.debug("Request to save Task : {}", taskDTO);
        String validationResult = check(taskDTO);
        // Chalasi bor GroupsDTO ga validatsiya qo'yilmadi!
        if (!validationResult.equals("OK")) {
            return new ResponseDTO<>(false, validationResult, null);
        }
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        taskDTO = taskMapper.toDto(task);

        return new ResponseDTO<>(true, "OK", taskDTO);
    }

    private String check(TaskDTO taskDTO) {
        List<String> contentTypes = List.of("image/png", "image/jpeg");
        if (taskDTO == null) {
            return "TaskDTO is null!";
        }
        if (taskDTO.getId() != null) {
            boolean isTaskExists = taskRepository.existsById(taskDTO.getId());
            return isTaskExists ? "Task is already exists!" : "Task is not found!";
        }
        //        if(!contentTypes.contains(taskDTO.getImageContentType()) && taskDTO.getImage() != null){
        //            return "Content-Type of image is wrong!";
        //        }
        return "OK";
    }

    private String check(GroupsDTO groupsDTO) {
        return groupValidator.newGroup(groupsDTO);
    }

    @Override
    public TaskDTO update(TaskDTO taskDTO) {
        log.debug("Request to update Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDTO> partialUpdate(TaskDTO taskDTO) {
        log.debug("Request to partially update Task : {}", taskDTO);

        return taskRepository
            .findById(taskDTO.getId())
            .map(existingTask -> {
                taskMapper.partialUpdate(existingTask, taskDTO);

                return existingTask;
            })
            .map(taskRepository::save)
            .map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> findAll() {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll().stream().map(MapperTaskImpl::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public ResponseDTO<CustomerTasksDTO> getOnlyOwnTasks() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            return new ResponseDTO<>(false, "User is not found!", null);
        }
        Optional<CustomerDTO> customerDTOOptional = customerService.findOneByUserLogin(user.getUsername());
        if (customerDTOOptional.isEmpty()) {
            return new ResponseDTO<>(false, "You have not group!", null);
        }
        CustomerDTO currentUser = customerDTOOptional.get();
        CustomerTasksDTO customerTasks = getUserTasks(currentUser);
        return new ResponseDTO<>(true, "OK", customerTasks);
    }

    private CustomerTasksDTO getUserTasks(CustomerDTO customer) {
        List<TaskDTO> completed = taskRepository
            .getCompletedTasks(customer.getId())
            .stream()
            .map(MapperTaskImpl::toDto)
            .collect(Collectors.toList());

        List<TaskDTO> notCompleted = taskRepository
            .getNotCompletedTasks(customer.getId())
            .stream()
            .map(MapperTaskImpl::toDto)
            .collect(Collectors.toList());

        CustomerTasksDTO customerTasks = new CustomerTasksDTO();
        customerTasks.setCompletedTasks(completed);
        customerTasks.setNotCompleted(notCompleted);
        return customerTasks;
    }

    @Override
    public ResponseDTO<CustomerTasksDTO> getCustomerTasks(String login, Long id) {
        log.debug("Admin going to get tasks of User");
        CustomerDTO customerDTO = null;
        if (login != null && id == null) {
            // Todo: Login is not null but ID is null! Find by login.
            Optional<CustomerDTO> customerOptional = customerService.findOneByUserLogin(login);
            if (customerOptional.isEmpty()) {
                // Todo: Return ResponseDTO with message "User is not found, Login is invalid!";
                return new ResponseDTO<>(false, "User is not found, Login is invalid!", null);
            }
            customerDTO = customerOptional.get();
        } else if (login == null && id != null) {
            // Todo: Login is null but ID is not null! Find by id.
            Optional<CustomerDTO> customerOptional = customerService.findOneByUserId(id);
            if (customerOptional.isEmpty()) {
                // Todo: Return ResponseDTO with message "User is not found, ID is invalid!";
                return new ResponseDTO<>(false, "User is not found, ID is invalid!", null);
            }
            customerDTO = customerOptional.get();
        } else {
            Optional<CustomerDTO> customerOptional = customerService.findOneByLoginAndId(login, id);
            if (customerOptional.isEmpty()) {
                //Todo: Return ResponseDTO with message "Login and ID was not belong to one User!";
                return new ResponseDTO<>(false, "Login and ID was not belong to one User!", null);
            }
            customerDTO = customerOptional.get();
        }
        CustomerTasksDTO customerTasks = getUserTasks(customerDTO);
        log.debug(
            "Customer ID: {} | Completed tasks count: {} | Not completed tasks count: {}",
            customerDTO.getId(),
            customerTasks.getCompletedTasks().size(),
            customerTasks.getNotCompleted().size()
        );
        return new ResponseDTO<>(true, "OK", customerTasks);
    }

    public Page<TaskDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taskRepository.findAllWithEagerRelationships(pageable).map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findOneWithEagerRelationships(id).map(taskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> getTasksByGroupId(Long groupId) {
        List<TaskDTO> tasks = taskRepository
            .findAllByGroups(groupId)
            .stream()
            .map(MapperTaskImpl::toDtoWithoutGroups)
            .collect(Collectors.toList());

        return tasks;
    }

    public List<TaskDTO> completedTasks(Long customerId) {
        if (customerId != null) {
            List<Task> tasks = taskRepository.getCompletedTasks(customerId);
            List<TaskDTO> dtoList = tasks.stream().map(MapperTaskImpl::toDtoWithoutGroups).collect(Collectors.toList());
            return dtoList;
        }
        //        throw CustomerExceptions.idNull();
        return null;
    }

    public List<TaskDTO> notCompletedTasks(Long customerId) {
        if (customerId != null) {
            List<Task> tasks = taskRepository.getNotCompletedTasks(customerId);
            List<TaskDTO> dtoList = tasks.stream().map(MapperTaskImpl::toDtoWithoutGroups).collect(Collectors.toList());
            return dtoList;
        }
        //        throw CustomerExceptions.idNull();
        return null;
    }
}
