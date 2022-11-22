package todo.manager.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Customer;
import todo.manager.domain.User;
import todo.manager.repository.CustomerRepository;
import todo.manager.service.CustomerService;
import todo.manager.service.GroupsService;
import todo.manager.service.UserService;
import todo.manager.service.dto.*;
import todo.manager.service.mapper.CustomerMapper;
import todo.manager.service.mapper.impl.MapperCustomerImpl;
import todo.manager.service.mapper.impl.MapperGroupsImpl;
import todo.manager.service.mapper.impl.MapperUserImpl;
import todo.manager.service.validation.GroupValidator;
import todo.manager.web.rest.errors.GroupsExceptions;

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

    private final GroupValidator groupValidator;

    public CustomerServiceImpl(
        CustomerRepository customerRepository,
        CustomerMapper customerMapper,
        UserService userService,
        GroupsService groupsService,
        GroupValidator groupValidator
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.userService = userService;
        this.groupsService = groupsService;
        this.groupValidator = groupValidator;
    }

    @Override
    public ResponseDTO<CustomerDTO> save(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);

        ResponseDTO<CustomerDTO> responseDTO = check(customerDTO.getUser());
        if (responseDTO != null) {
            return responseDTO;
        }
        responseDTO = check(customerDTO.getGroup());
        if (responseDTO != null) {
            return responseDTO;
        }

        UserDTO userDTO = findUser(customerDTO.getUser());
        GroupsDTO groupsDTO = findGroup(customerDTO.getGroup());
        if (groupsDTO == null) {
            return new ResponseDTO<>(false, "Groups is not found!", null);
        }
        customerDTO.setUser(userDTO);
        customerDTO.setGroup(groupsDTO);

        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        customerDTO = MapperCustomerImpl.toDTO(customer);

        return new ResponseDTO<>(true, "OK", customerDTO);
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

    @Override
    public List<CustomerDTO> getCustomerByGroupId(Long groupId) {
        Optional<List<Customer>> optional = customerRepository.findAllByGroupId(groupId);

        List<CustomerDTO> customerDTOS = optional.get().stream().map(customerMapper::toDto).collect(Collectors.toList());
        return customerDTOS;
    }

    private ResponseDTO<CustomerDTO> check(UserDTO userDTO) {
        String validationResult = checkUser(userDTO);
        if (!validationResult.equals("OK")) {
            return new ResponseDTO<>(false, validationResult, null);
        }
        return null;
    }

    private ResponseDTO<CustomerDTO> check(GroupsDTO groupsDTO) {
        List<String> results = List.of("OK", "Group is already exists!");
        String validationResult = groupValidator.newGroup(groupsDTO);
        if (!results.contains(validationResult)) {
            return new ResponseDTO<>(false, validationResult, null);
        }
        return null;
    }

    private UserDTO findUser(UserDTO userDTO) {
        Optional<User> optional = null;

        String login = String.valueOf(userDTO.getLogin());
        optional = userService.getUserWithAuthoritiesByLogin(login);

        User user = null;
        if (optional.isEmpty()) {
            AdminUserDTO adminUserDTO = MapperUserImpl.userDtoToAdminDto(userDTO);
            user = userService.registerUser(adminUserDTO, userDTO.getPassword());
        } else {
            user = optional.get();
        }
        userDTO.setId(user.getId());
        return userDTO;
    }

    private String checkUser(UserDTO userDTO) {
        if (userDTO == null) {
            return "UserDTO is null!";
        } else if (userDTO.getLogin() == null) {
            return "Login of UserDTO is null!";
        } else if (userDTO.getLogin().trim().isEmpty()) {
            return "Login of UserDTO is empty!";
        }
        return "OK";
    }

    public GroupsDTO findGroup(GroupsDTO groupsDTO) {
        Optional<GroupsDTO> optional = groupsService.findOne(groupsDTO.getId());
        if (optional.isEmpty()) {
            ResponseDTO<GroupsDTO> responseDTO = groupsService.findByGroupName(groupsDTO.getGroupName());
            if (responseDTO.getSuccess() && responseDTO.getData() != null) {
                return responseDTO.getData();
            }
            return null;
        }
        return optional.get();
    }
}
