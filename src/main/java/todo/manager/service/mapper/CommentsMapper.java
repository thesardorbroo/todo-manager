package todo.manager.service.mapper;

import org.mapstruct.*;
import todo.manager.domain.Comments;
import todo.manager.domain.Task;
import todo.manager.service.dto.CommentsDTO;
import todo.manager.service.dto.TaskDTO;

/**
 * Mapper for the entity {@link Comments} and its DTO {@link CommentsDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentsMapper extends EntityMapper<CommentsDTO, Comments> {
    @Mapping(target = "task", source = "task", qualifiedByName = "taskId")
    CommentsDTO toDto(Comments s);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);
}
