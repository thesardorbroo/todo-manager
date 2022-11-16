package todo.manager.service.mapper;

import org.mapstruct.*;
import todo.manager.domain.Groups;
import todo.manager.service.dto.GroupsDTO;

/**
 * Mapper for the entity {@link Groups} and its DTO {@link GroupsDTO}.
 */
@Mapper(componentModel = "spring")
public interface GroupsMapper extends EntityMapper<GroupsDTO, Groups> {}
