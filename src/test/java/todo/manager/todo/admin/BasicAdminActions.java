package todo.manager.todo.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
//import org.junit.jupiter.api.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import todo.manager.todo.TodoList;
import todo.manager.web.rest.vm.LoginVM;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("C:\\Users\\thesardorbroo\\JHipster_project\\target\\test-classes\\config\\application-testdev.yml")
abstract class BasicAdminActions implements TodoList {

    protected Logger log = LoggerFactory.getLogger(BasicAdminActions.class);
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected JsonMapper jsonMapper = new JsonMapper();

    @Autowired
    protected MockMvc mvc;

    //
    //    public BasicAdminActions(MockMvc mvc) {
    //        this.objectMapper = new ObjectMapper();
    //        this.jsonMapper = new JsonMapper();
    //        this.mvc = mvc;
    //    }

    protected static String adminKey;

    @Test
    @Order(1)
    @Override
    public void authenticate() {
        LoginVM login = new LoginVM();
        login.setUsername("admin");
        login.setPassword("admin");

        try {
            String content = jsonMapper.writeValueAsString(login);

            RequestBuilder request = MockMvcRequestBuilders
                .post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

            Object headerValue = mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
                .andReturn()
                .getResponse()
                .getHeaderValue("Authorization");

            log.warn("Response: {} ", headerValue);

            Assertions.assertNotNull(headerValue);
            Assertions.assertInstanceOf(String.class, headerValue);

            String header = (String) headerValue;
            Assertions.assertTrue(header.contains("Bearer "));

            adminKey = header;
            log.info("Test was successfully!");
        } catch (JsonProcessingException e) {
            log.error("Error while parsing LoginVM to String: {}", login);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.info("Error while sending request with MockMvc.perform()");
            throw new RuntimeException(e);
        }
    }

    abstract void createGroups() throws Exception;

    abstract void createCustomer() throws Exception;

    abstract void createTaskForGroups();

    abstract void getStatusesOfTasks();
}
