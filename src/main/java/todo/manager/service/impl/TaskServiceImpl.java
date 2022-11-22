package todo.manager.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Task;
import todo.manager.repository.TaskRepository;
import todo.manager.service.TaskService;
import todo.manager.service.dto.GroupsDTO;
import todo.manager.service.dto.ResponseDTO;
import todo.manager.service.dto.TaskDTO;
import todo.manager.service.mapper.TaskMapper;
import todo.manager.service.mapper.impl.MapperTaskImpl;
import todo.manager.service.validation.GroupValidator;
import todo.manager.web.rest.errors.CustomerExceptions;

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

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, GroupValidator groupValidator) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.groupValidator = groupValidator;
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
