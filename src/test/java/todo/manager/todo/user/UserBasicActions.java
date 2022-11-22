package todo.manager.todo.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
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
import todo.manager.IntegrationTest;
import todo.manager.todo.TodoList;
import todo.manager.web.rest.vm.LoginVM;

@IntegrationTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@PropertySource("classpath:/config/application-dev.yml")
abstract class UserBasicActions implements TodoList {

    protected Logger log = LoggerFactory.getLogger(UserBasicActions.class);

    @Autowired
    private MockMvc mvc;

    protected JsonMapper jsonMapper = new JsonMapper();

    protected ObjectMapper objectMapper = new ObjectMapper();

    //    public UserBasicActions(MockMvc mvc) {
    //        this.mvc = mvc;
    //        this.jsonMapper = new JsonMapper();
    //        this.objectMapper = new JsonMapper();
    //    }

    protected static String userKey;

    @Test
    @Order(1)
    @Override
    public void authenticate() {
        //        log.info("{}", getClass().getClassLoader().get);
        //        this.jsonMapper = new JsonMapper();
        //        this.objectMapper = new ObjectMapper();

        LoginVM login = new LoginVM();
        login.setUsername("user");
        login.setPassword("user");

        String content = null;
        try {
            content = jsonMapper.writeValueAsString(login);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBuilder request = MockMvcRequestBuilders.post("/api/authenticate").content(content).contentType(MediaType.APPLICATION_JSON);

        String contentAsString = null;
        try {
            contentAsString =
                (String) mvc
                    .perform(request)
                    .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
                    .andReturn()
                    .getResponse()
                    .getHeaderValue("Authorization");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertNotNull(contentAsString);
        Assertions.assertTrue(contentAsString.contains("Bearer "));

        userKey = contentAsString;
        log.info("[Authenticating was successfully] Token: {}", userKey);
    }

    @Override
    public void deleteAll() {}

    abstract void getAllTasks();

    abstract void markTasks();

    abstract void useGetAllTasksAgain();
}
