package todo.manager.service.mapper.impl;

import java.util.stream.Collectors;
import org.mapstruct.Named;
import todo.manager.domain.Task;
import todo.manager.service.dto.TaskDTO;

public class MapperTaskImpl {

    public static TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();

        dto.setId(task.getId());
        dto.setBody(task.getBody());
        dto.setCaption(task.getCaption());
        dto.setImage(task.getImage());
        dto.setImageContentType(task.getImageContentType());
        dto.setGroups(task.getGroups().stream().map(MapperGroupsImpl::toDtoWithoutTask).collect(Collectors.toSet()));

        return dto;
    }

    public static TaskDTO toDtoWithoutGroups(Task task) {
        TaskDTO dto = new TaskDTO();

        dto.setId(task.getId());
        dto.setBody(task.getBody());
        dto.setCaption(task.getCaption());
        dto.setImage(task.getImage());
        dto.setImageContentType(task.getImageContentType());

        return dto;
    }
}
