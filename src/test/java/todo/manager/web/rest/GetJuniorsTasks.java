package todo.manager.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Random;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
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
@WithMockUser
public class GetJuniorsTasks {

    private final Logger log = LoggerFactory.getLogger(GetJuniorsTasks.class);
    private final JsonMapper jsonMapper = new JsonMapper();
    private final String API = "/api/junior-tasks/";
    private static String authorizationKey;

    private final String DEFAULT_USER_USERNAME = "sardorbroo";
    private final String DEFAULT_USER_PASSWORD = "sardorceek12";
    private final String DEFAULT_USER_EMAIL = "thesardorisfire@gmail.com";

    // Repositories
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

    // Utils
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mvc;

    // Entities
    private static User user;
    private static Task task;
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
        //        if(user == null) {
        User demoUser = createUserEntity();
        user = userRepository.saveAndFlush(demoUser);
        log.info("User is saved successfully, User: {}", user);
        //        }
        //        if(group == null) {
        Groups demoGroup = createGroupEntity();
        group = groupsRepository.saveAndFlush(demoGroup);
        log.info("Group is saved successfully, Group: {}", group);
        //        }
        //        if(customer == null) {
        Customer demoCustomer = createCustomerEntity();
        customer = customerRepository.saveAndFlush(demoCustomer);
        log.info("Customer is saved successfully, Customer: {}", customer);
        //        }
        //        if(task == null){
        Task demoTask = createTaskEntity();
        task = taskRepository.saveAndFlush(demoTask);
        log.info("Task is saved successfully, Task: {}", task);
        //        }
    }

    @Test
    @Order(1)
    @Transactional
    public void authenticate() throws Exception {
        LoginVM login = new LoginVM();
        login.setUsername(DEFAULT_USER_USERNAME);
        login.setPassword(DEFAULT_USER_PASSWORD);

        String content = objectAsString(login);
        RequestBuilder request = MockMvcRequestBuilders
            .post("/api/authenticate/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
        String headerValue = (String) mvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
            .andReturn()
            .getResponse()
            .getHeaderValue("Authorization");

        Assertions.assertNotNull(headerValue);
        Assertions.assertTrue(headerValue.contains("Bearer "));
        authorizationKey = headerValue;
    }

    @Test
    @Order(2)
    @Transactional
    public void sendRequestWithoutParameters() throws Exception {
        RequestBuilder request = createRequestBuilder(null, null, null);
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        log.info("Response as string: {}", responseAsString);

        ResponseDTO<CustomerTasksDTO> response = stringAsObject(responseAsString);
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getSuccess());
        Assertions.assertTrue(response.getMessage().contains("are not given"));
        Assertions.assertNull(response.getData());

        log.debug("Test worked successfully! Send request without any parameters, Response: {}", response);
    }

    @Test
    @Order(3)
    @Transactional
    public void requestWithInvalidId() throws Exception {
        Random random = new Random();
        RequestBuilder request = createRequestBuilder(null, random.nextLong(), null);
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        ResponseDTO<CustomerTasksDTO> response = stringAsObject(responseAsString);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getSuccess());
        Assertions.assertTrue(response.getMessage().contains("ID is invalid"));
        Assertions.assertNull(response.getData());
    }

    @Test
    @Order(4)
    @Transactional
    public void requestWithValidId() throws Exception {
        RequestBuilder request = createRequestBuilder(null, user.getId(), null);
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        ResponseDTO<CustomerTasksDTO> response = stringAsObject(responseAsString);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertTrue(response.getMessage().contains("OK"));
        Assertions.assertNotNull(response.getData());
        Assertions.assertInstanceOf(CustomerTasksDTO.class, response.getData());
    }

    @Test
    @Order(5)
    @Transactional
    public void requestWithInvalidLogin() throws Exception {
        RequestBuilder request = createRequestBuilder(null, null, "poqjfwkcdslkcna");
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        ResponseDTO<CustomerTasksDTO> response = stringAsObject(responseAsString);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getSuccess());
        Assertions.assertTrue(response.getMessage().contains("is invalid"));
        Assertions.assertNull(response.getData());

        log.debug("Test worked successfully! Sending request with invalid login, Response: {}", response);
    }

    @Test
    @Order(6)
    @Transactional
    public void requestWithValidLogin() throws Exception {
        RequestBuilder request = createRequestBuilder(null, null, DEFAULT_USER_USERNAME);
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        ResponseDTO<CustomerTasksDTO> response = stringAsObject(responseAsString);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertTrue(response.getMessage().contains("OK"));
        Assertions.assertNotNull(response.getData());
        Assertions.assertInstanceOf(CustomerTasksDTO.class, response.getData());

        log.debug("Test worked successfully! Sending request with valid login, Response: {}", response);
    }

    @Test
    @Order(7)
    @Transactional
    public void giveBothFakeParameters() throws Exception {
        Random random = new Random();
        RequestBuilder request = createRequestBuilder(null, random.nextLong(), DEFAULT_USER_USERNAME);
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        ResponseDTO<CustomerTasksDTO> response = stringAsObject(responseAsString);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getSuccess());
        Assertions.assertTrue(response.getMessage().contains("not belong to one User"));
        Assertions.assertNull(response.getData());

        log.debug(
            "Test worked successfully! Sending request with login and id but one parameter isn't belong to one User, Response: {}",
            response
        );
    }

    @Test
    @Order(8)
    @Transactional
    public void giveBothValidParameters() throws Exception {
        RequestBuilder request = createRequestBuilder(null, user.getId(), DEFAULT_USER_USERNAME);
        String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
        ResponseDTO<CustomerTasksDTO> response = stringAsObject(responseAsString);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertTrue(response.getMessage().contains("OK"));
        Assertions.assertNotNull(response.getData());
        Assertions.assertInstanceOf(CustomerTasksDTO.class, response.getData());

        log.debug("Test worked successfully! Sending request with bot valid parameters, Response: {}", response);
    }

    private String objectAsString(Object obj) throws JsonProcessingException {
        String content = jsonMapper.writeValueAsString(obj);
        log.debug("Object as string: {}", content);
        return content;
    }

    private ResponseDTO<CustomerTasksDTO> stringAsObject(String contentAsString) throws JsonProcessingException {
        return jsonMapper.readValue(contentAsString, new TypeReference<ResponseDTO<CustomerTasksDTO>>() {});
    }

    private RequestBuilder createRequestBuilder(String content, Long customerId, String login) throws URISyntaxException {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(API)
            .content(content == null ? "" : content)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorizationKey == null ? "" : authorizationKey);

        if (customerId != null) {
            request.param("id", String.valueOf(customerId));
        }
        if (login != null) {
            request.param("login", login);
        }
        log.info(
            "RequestBuilder is created, Http method: {} | URL: {} | Parameter ID of customer: {} | Parameter login: {} | Content: {}",
            HttpMethod.GET,
            API,
            customerId,
            login,
            content
        );
        return request;
    }
}
