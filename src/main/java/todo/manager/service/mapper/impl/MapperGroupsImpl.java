package todo.manager.service.mapper.impl;

import java.util.stream.Collectors;
import org.mapstruct.Named;
import todo.manager.domain.Groups;
import todo.manager.service.dto.GroupsDTO;

public class MapperGroupsImpl {

    public static GroupsDTO toDto(Groups groups) {
        GroupsDTO dto = new GroupsDTO();

        dto.setId(groups.getId());
        dto.setGroupName(groups.getGroupName());
        dto.setTasks(groups.getTasks().stream().map(MapperTaskImpl::toDtoWithoutGroups).collect(Collectors.toSet()));

        return dto;
    }

    public static GroupsDTO toDtoWithoutTask(Groups groups) {
        GroupsDTO dto = new GroupsDTO();
        dto.setId(groups.getId());
        dto.setGroupName(groups.getGroupName());

        return dto;
    }
}
