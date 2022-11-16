package todo.manager.service.mapper;

import org.mapstruct.*;
import todo.manager.domain.Customer;
import todo.manager.domain.Groups;
import todo.manager.domain.User;
import todo.manager.service.dto.CustomerDTO;
import todo.manager.service.dto.GroupsDTO;
import todo.manager.service.dto.UserDTO;
import todo.manager.service.mapper.impl.MapperGroupsImpl;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "group", source = "group", qualifiedByName = "groupsId")
    CustomerDTO toDto(Customer s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("groupsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GroupsDTO toDtoGroupsId(Groups groups);
    //    @Named("to-dto-without-task")
    //    private GroupsDTO toGroupDto(Groups groups){
    //        return MapperGroupsImpl.toDtoWithoutTask(groups);
    //    }
}
