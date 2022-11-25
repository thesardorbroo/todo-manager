package todo.manager.web.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.IntegrationTest;
import todo.manager.domain.*;
import todo.manager.repository.*;
import todo.manager.security.AuthoritiesConstants;
import todo.manager.service.dto.CustomerTasksDTO;
import todo.manager.service.dto.ResponseDTO;

@WithMockUser
@IntegrationTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WholeProcess {

    // Fields
    private final String DEFAULT_USER_USERNAME = "sardorbroo";
    private final String DEFAULT_USER_PASSWORD = "sardorceek12";
    private final String DEFAULT_USER_EMAIL = "thesardorisfire@gmail.com";
    private final Logger log = LoggerFactory.getLogger(WholeProcess.class);
    private final JsonMapper jsonMapper = new JsonMapper();
    private static String authorizationKey;

    @Autowired
    // Repositories
    private TodoRepository todoRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    // Entities
    private static User user;
    private static Task task;
    private static Groups group;
    private static Customer customer;

    // Utils
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mvc;

    private void setAuthoritiesToUser(User entity) {
        Set<Authority> userAuthorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(userAuthorities::add);
        entity.setAuthorities(userAuthorities);
    }

    private User createUserEntity() {
        User entity = new User();
        entity.setFirstName("First name");
        entity.setLastName("Last name");
        entity.setLogin(DEFAULT_USER_USERNAME);
        entity.setPassword(encoder.encode(DEFAULT_USER_PASSWORD));
        entity.setEmail(DEFAULT_USER_EMAIL);
        entity.setActivated(true);
        entity.setLangKey("en");
        setAuthoritiesToUser(entity);

        return entity;
    }

    private Groups createGroupEntity() {
        Groups groupEntity = new Groups();
        groupEntity.setGroupName("Devops internship");
        return groupEntity;
    }

    private Customer createCustomerEntity() {
        Customer customerEntity = new Customer();
        customerEntity.setGroup(group);
        customerEntity.setUser(user);
        return customerEntity;
    }

    private Task createTaskEntity() {
        Task taskEntity = new Task();
        taskEntity.setBody("Demo task");
        taskEntity.setCaption("Demo task for testing APIs");
        taskEntity.setGroups(Set.of(group));

        return taskEntity;
    }

    //    @BeforeEach
    //    @Transactional
    public void saveUserEntity() {
        if (user == null) {
            User demoUser = createUserEntity();
            user = userRepository.saveAndFlush(demoUser);
            log.info("User is saved successfully, User: {}", user);
        }
    }

    //    @Transactional
    public void saveGroupEntity() {
        if (group == null) {
            Groups demoGroup = createGroupEntity();
            group = groupsRepository.saveAndFlush(demoGroup);
            log.info("Group is saved successfully, Group: {}", group);
        }
    }

    //    @Transactional
    public void saveCustomerEntity() {
        if (customer == null) {
            Customer demoCustomer = createCustomerEntity();
            customer = customerRepository.saveAndFlush(demoCustomer);
            log.info("Customer is saved successfully, Customer: {}", customer);
        }
    }

    //    @Transactional
    public void saveTaskEntity() {
        if (task == null) {
            Task demoTask = createTaskEntity();
            task = taskRepository.saveAndFlush(demoTask);
            log.info("Task is saved successfully, Task: {}", task);
        }
    }

    @Test
    @Order(1)
    @Transactional
    public void breaksWhenAdminBindJuniorToGroup() throws Exception {
        saveUserEntity();
        saveGroupEntity();
        saveCustomerEntity();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/mine-tasks").contentType(MediaType.APPLICATION_JSON);
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        ResponseDTO<CustomerTasksDTO> response = jsonMapper.readValue(
            responseAsString,
            new TypeReference<ResponseDTO<CustomerTasksDTO>>() {}
        );

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getSuccess());
        Assertions.assertNull(response.getData());

        log.info("Test worked successfully! Response: {}", response);
    }
}
