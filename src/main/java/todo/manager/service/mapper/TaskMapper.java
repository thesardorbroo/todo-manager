package todo.manager.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import todo.manager.domain.Groups;
import todo.manager.domain.Task;
import todo.manager.service.dto.GroupsDTO;
import todo.manager.service.dto.TaskDTO;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "groups", source = "groups", qualifiedByName = "groupsIdSet")
    TaskDTO toDto(Task s);

    //    @Mapping(target = "removeGroups", ignore = true)
    Task toEntity(TaskDTO taskDTO);

    @Named("groupsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GroupsDTO toDtoGroupsId(Groups groups);

    @Named("groupsIdSet")
    default Set<GroupsDTO> toDtoGroupsIdSet(Set<Groups> groups) {
        return groups.stream().map(this::toDtoGroupsId).collect(Collectors.toSet());
    }
}
