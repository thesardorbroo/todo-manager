package todo.manager.todo.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.*;
//import org.junit.Test;
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
import todo.manager.service.dto.*;
import todo.manager.web.rest.vm.LoginVM;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("classpath:/config/application-dev.yml")
public class TestForAdmin {

    private final Logger log = LoggerFactory.getLogger(TestForAdmin.class);

    private static String adminKey;

    private static String userKey;

    @Autowired
    private MockMvc mvc;

    private LoginVM login;

    private JsonMapper jsonMapper;

    private ObjectMapper objectMapper;

    private static GroupsDTO groupsDTO;

    private static CustomerDTO customerDTO;

    private static TaskDTO taskDTO;

    @PostConstruct
    public void inject() {
        this.login = new LoginVM();
        login.setUsername("admin");
        login.setPassword("admin");

        this.jsonMapper = new JsonMapper();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void authenticateAdmin() {
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

    @Test
    @Order(2)
    public void createGroups() {
        groupsDTO = new GroupsDTO();
        groupsDTO.setGroupName("Test group");

        try {
            String content = jsonMapper.writeValueAsString(groupsDTO);
            log.info("GroupDTO as string: {}", content);
            RequestBuilder request = MockMvcRequestBuilders
                .post("/api/groups")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminKey);

            String responseAsString = mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

            ResponseDTO<GroupsDTO> response = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<GroupsDTO>>() {});

            Assertions.assertNotNull(response);
            Assertions.assertTrue(response.getSuccess());
            Assertions.assertNotNull(response.getData());
            Assertions.assertInstanceOf(GroupsDTO.class, response.getData());
            groupsDTO = response.getData();

            //            dtoMap.put("groupDTO", groupsDTO);
            log.info("[Creating groups was successfully] GroupDTO: {}", groupsDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    //    @ParameterizedTest
    //    @ValueSource(strings = {"en", "ru", "uz"})
    @Order(3)
    public void createCustomer() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("First name");
        userDTO.setLastName("Last name");
        userDTO.setEmail("test@gmail.com");
        userDTO.setLogin("login");
        userDTO.setPassword("password");
        userDTO.setLangKey("en");

        customerDTO = new CustomerDTO();
        customerDTO.setGroup(groupsDTO);
        customerDTO.setUser(userDTO);

        try {
            String content = jsonMapper.writeValueAsString(customerDTO);
            log.info("CustomerDTO as string: {}", content);
            RequestBuilder request = MockMvcRequestBuilders
                .post("/api/customers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", adminKey);

            String responseAsString = mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

            ResponseDTO<CustomerDTO> responseDTO = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<CustomerDTO>>() {});

            Assertions.assertNotNull(responseDTO);
            Assertions.assertTrue(responseDTO.getSuccess());
            Assertions.assertNotNull(responseDTO.getData());
            Assertions.assertInstanceOf(CustomerDTO.class, responseDTO.getData());
            customerDTO = responseDTO.getData();

            log.info("[Adding CustomerDTO was successfully] CustomerDTO: {}", customerDTO);
        } catch (JsonProcessingException e) {
            log.info("Error while parsing DTO to String");
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    public void createTaskForGroups() {
        taskDTO = new TaskDTO();
        taskDTO.setBody("New Demo Task");
        taskDTO.setCaption("Demo caption");
        taskDTO.setGroups(Set.of(groupsDTO));

        try {
            String content = jsonMapper.writeValueAsString(taskDTO);
            RequestBuilder request = MockMvcRequestBuilders
                .post("/api/tasks/")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminKey)
                .accept(MediaType.APPLICATION_JSON);

            String responseAsString = mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

            ResponseDTO<TaskDTO> responseDTO = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<TaskDTO>>() {});

            Assertions.assertNotNull(responseDTO);
            Assertions.assertTrue(responseDTO.getSuccess());
            Assertions.assertNotNull(responseDTO.getData());
            Assertions.assertInstanceOf(TaskDTO.class, responseDTO.getData());

            taskDTO = responseDTO.getData();

            log.info("[Adding task was successfully] KEY: {} | VALUE: {}", "taskDTO", taskDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    @Test
    //    @Order(6)
    //    public void markTasks() {
    ////        TaskDTO taskDTO = (TaskDTO) dtoMap.get("taskDTO");
    //
    //        RequestBuilder request = MockMvcRequestBuilders.post("/api/todo/mark?id=" + taskDTO.getId())
    //            .header("Authorization", adminKey).contentType(MediaType.APPLICATION_JSON);
    //
    //        try {
    //            mvc
    //                .perform(request)
    //                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    //
    //
    //        } catch (Exception e) {
    //            throw new RuntimeException(e);
    //        }
    //
    //
    //    }

    @Test
    @Order(7)
    public void getStatusesOfTasks() {
        RequestBuilder request = MockMvcRequestBuilders
            .get("/api/todo/result/" + groupsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", adminKey);

        try {
            String responseAsString = mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

            ResponseDTO<List<CustomerResultDTO>> responseDTO = objectMapper.readValue(
                responseAsString,
                new TypeReference<ResponseDTO<List<CustomerResultDTO>>>() {}
            );

            Assertions.assertNotNull(responseDTO);

            Assertions.assertTrue(responseDTO.getSuccess());
            Assertions.assertNotNull(responseDTO.getData());
            Assertions.assertInstanceOf(List.class, responseDTO.getData());
            List<CustomerResultDTO> list = (List<CustomerResultDTO>) responseDTO.getData();

            int i = 0;
            for (CustomerResultDTO resultDTO : list) {
                log.info("Element index: {} | CustomerResultDTO: {}", i++, resultDTO);

                int doneTasks = resultDTO.getDone();
                int countOfTasks = resultDTO.getTasksCount();
                int count = 0;

                List<TaskResultDTO> tasks = resultDTO.getTasks();
                for (TaskResultDTO t : tasks) {
                    Boolean isDone = t.getResult();
                    count += isDone ? 1 : 0;
                    log.info(
                        "Customer ID: {} | Task ID: {} | isDone: {} | Count: {}",
                        resultDTO.getCustomer().getId(),
                        t.getTask().getId(),
                        isDone,
                        count
                    );
                }

                Assertions.assertEquals(countOfTasks, tasks.size());
                Assertions.assertEquals(doneTasks, count);
                log.info("Total count of tasks: {} | Done: {} | Count: {} | The result: {}", countOfTasks, doneTasks, count, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    public void deleteAll() {
        long groupId = groupsDTO.getId();

        long userId = customerDTO.getUser().getId();
        long taskId = taskDTO.getId();

        try {
            RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/customers/" + customerDTO.getId())
                .header("Authorization", adminKey);
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().is(204));

            request =
                MockMvcRequestBuilders
                    .delete("/api/admin/users/" + customerDTO.getUser().getLogin())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminKey);
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().is(204));

            request = MockMvcRequestBuilders.delete("/api/tasks/" + taskId).header("Authorization", adminKey);
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().is(204));

            request = MockMvcRequestBuilders.delete("/api/groups/" + groupId).header("Authorization", adminKey);
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().is(204));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
//    @Test
//    @Order(5)
//    @Override
//    public void getTasksOnlyMine() {
//        RequestBuilder request = MockMvcRequestBuilders.get("/api/todo/tasks/")
//            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("Authorization", adminKey);
//
//        try {
//            mvc
//                .perform(request)
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
