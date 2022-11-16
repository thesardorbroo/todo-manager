package todo.manager.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Customer;
import todo.manager.domain.Groups;
import todo.manager.domain.User;
import todo.manager.repository.CustomerRepository;
import todo.manager.service.CustomerService;
import todo.manager.service.GroupsService;
import todo.manager.service.UserService;
import todo.manager.service.dto.AdminUserDTO;
import todo.manager.service.dto.CustomerDTO;
import todo.manager.service.dto.GroupsDTO;
import todo.manager.service.dto.UserDTO;
import todo.manager.service.mapper.CustomerMapper;
import todo.manager.service.mapper.UserMapper;
import todo.manager.service.mapper.impl.MapperGroupsImpl;
import todo.manager.service.mapper.impl.MapperUserImpl;

/**
 * Service Implementation for managing {@link Customer}.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final UserService userService;

    private final GroupsService groupsService;

    public CustomerServiceImpl(
        CustomerRepository customerRepository,
        CustomerMapper customerMapper,
        UserService userService,
        GroupsService groupsService
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.userService = userService;
        this.groupsService = groupsService;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);
        AdminUserDTO adminUserDTO = MapperUserImpl.userDtoToAdminDto(customerDTO.getUser());

        User user = userService.registerUser(adminUserDTO, customerDTO.getUser().getPassword());

        customerDTO.getUser().setId(user.getId());
        customerDTO.getUser().setLogin(user.getLogin());

        Optional<GroupsDTO> optional = groupsService.findOne(customerDTO.getGroup().getId());
        if (optional.isEmpty()) {
            GroupsDTO groupsDTO = new GroupsDTO();
            groupsDTO.setId(customerDTO.getId());
            if (customerDTO.getGroup().getGroupName() != null) {
                groupsDTO.setGroupName(customerDTO.getGroup().getGroupName());
                groupsDTO = groupsService.save(groupsDTO);
                customerDTO.setGroup(groupsDTO);
            }
        }

        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        log.debug("Request to update Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Override
    public Optional<CustomerDTO> partialUpdate(CustomerDTO customerDTO) {
        log.debug("Request to partially update Customer : {}", customerDTO);

        return customerRepository
            .findById(customerDTO.getId())
            .map(existingCustomer -> {
                customerMapper.partialUpdate(existingCustomer, customerDTO);

                return existingCustomer;
            })
            .map(customerRepository::save)
            .map(customerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        log.debug("Request to get all Customers");
        return customerRepository.findAll().stream().map(customerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    @Override
    public Optional<CustomerDTO> findOneByUserLogin(String login) {
        Optional<Customer> optional = customerRepository.getByUserLogin(login);
        if (optional.isPresent()) {
            Customer customer = optional.get();
            UserDTO userDTO = MapperUserImpl.toDto(customer.getUser());
            GroupsDTO groupsDTO = MapperGroupsImpl.toDto(customer.getGroup());

            CustomerDTO dto = new CustomerDTO();
            dto.setId(customer.getId());
            dto.setUser(userDTO);
            dto.setGroup(groupsDTO);

            return Optional.ofNullable(dto);
        }

        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
    }
}
