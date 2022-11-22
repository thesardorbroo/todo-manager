package todo.manager.service.mapper.impl;

import todo.manager.domain.Customer;
import todo.manager.service.dto.CustomerDTO;
import todo.manager.service.mapper.UserMapper;

public class MapperCustomerImpl {

    //    @Autowired
    private static UserMapper userMapper = new UserMapper();

    public static CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUser(userMapper.userToUserDTO(customer.getUser()));
        dto.setGroup(MapperGroupsImpl.toDtoWithoutTask(customer.getGroup()));
        dto.setId(customer.getId());

        return dto;
    }
}
