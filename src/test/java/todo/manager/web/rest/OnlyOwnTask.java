package todo.manager.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import todo.manager.IntegrationTest;
import todo.manager.domain.*;
import todo.manager.repository.*;
import todo.manager.security.AuthoritiesConstants;
import todo.manager.service.dto.CustomerTasksDTO;
import todo.manager.service.dto.ResponseDTO;
import todo.manager.web.rest.vm.LoginVM;

@IntegrationTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OnlyOwnTask {

    private final Logger log = LoggerFactory.getLogger(OnlyOwnTask.class);

    private final JsonMapper jsonMapper = new JsonMapper();
    private final String DEFAULT_USER_USERNAME = "sardorbroo";
    private final String DEFAULT_USER_PASSWORD = "sardorceek12";
    private final String DEFAULT_USER_EMAIL = "thesardorisfire@gmail.com";
    private static String authorizationKey;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mvc;

    // Entities
    private static Task task;
    private static User user;
    private static Groups group;
    private static Customer customer;

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

    @BeforeEach
    public void saveEntities() {
        if (user == null) {
            User demoUser = createUserEntity();
            user = userRepository.saveAndFlush(demoUser);
            log.info("User is saved successfully, User: {}", user);
        }
        if (group == null) {
            Groups demoGroup = createGroupEntity();
            group = groupsRepository.saveAndFlush(demoGroup);
            log.info("Group is saved successfully, Group: {}", group);
        }
        if (customer == null) {
            Customer demoCustomer = createCustomerEntity();
            customer = customerRepository.saveAndFlush(demoCustomer);
            log.info("Customer is saved successfully, Customer: {}", customer);
        }
        if (task == null) {
            Task demoTask = createTaskEntity();
            task = taskRepository.saveAndFlush(demoTask);
            log.info("Task is saved successfully, Task: {}", task);
        }
    }

    @Test
    @Order(1)
    public void authenticate() throws Exception {
        LoginVM login = new LoginVM();
        login.setUsername(DEFAULT_USER_USERNAME);
        login.setPassword(DEFAULT_USER_PASSWORD);

        String content = objectAsString(login);
        RequestBuilder request = createRequestBuilder(HttpMethod.POST, content, "/api/authenticate");
        String authorizationHeader = (String) mvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
            .andReturn()
            .getResponse()
            .getHeaderValue("Authorization");

        Assertions.assertNotNull(authorizationHeader);
        Assertions.assertTrue(authorizationHeader.contains("Bearer "));
        authorizationKey = authorizationHeader;
    }

    @Test
    @Order(2)
    public void gettingTaskWithoutProblems() throws Exception {
        RequestBuilder request = createRequestBuilder(HttpMethod.GET, null, "/api/mine-tasks");
        String contentAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();

        ResponseDTO<CustomerTasksDTO> response = jsonMapper.readValue(
            contentAsString,
            new TypeReference<ResponseDTO<CustomerTasksDTO>>() {}
        );

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertNotNull(response.getData());
        log.debug("Response: {}", response);
        log.debug("Getting own tasks was successfully");
    }

    private String objectAsString(Object obj) throws JsonProcessingException {
        String content = jsonMapper.writeValueAsString(obj);
        log.debug("Object as string: {}", content);
        return content;
    }

    private RequestBuilder createRequestBuilder(HttpMethod method, String content, String url) throws URISyntaxException {
        log.debug("[Creating RequestBuilder] Http method: {} | Content: {} | URL: {}", method, content, url);
        return MockMvcRequestBuilders
            .request(method, new URI(url))
            .content(content == null ? "" : content)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorizationKey == null ? "" : authorizationKey);
    }
}
