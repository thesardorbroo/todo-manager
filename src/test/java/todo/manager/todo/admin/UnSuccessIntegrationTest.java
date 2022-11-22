package todo.manager.todo.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import todo.manager.service.dto.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("C:\\Users\\thesardorbroo\\JHipster_project\\target\\test-classes\\config\\application-testdev.yml")
public class UnSuccessIntegrationTest extends BasicAdminActions {

    private static GroupsDTO groupsDTO;

    private static CustomerDTO customerDTO;

    private static TaskDTO taskDTO;

    //    public UnSuccessIntegrationTest(MockMvc mvc) {
    //        super(mvc);
    //    }

    @Test
    @Order(2)
    @Override
    void createGroups() {
        String content = fromObjectToString(groupsDTO);
        RequestBuilder request = MockMvcRequestBuilders
            .post("/api/groups/")
            .content(content)
            .header("Authorization", adminKey)
            .contentType(MediaType.APPLICATION_JSON);
        String responseAsString = parseResponseToString(request, true);

        Assertions.assertTrue(responseAsString.contains("Bad Request"));

        groupsDTO = new GroupsDTO();
        groupsDTO.setId((long) 7894651);

        content = fromObjectToString(groupsDTO);
        request =
            MockMvcRequestBuilders
                .post("/api/groups/")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminKey);
        responseAsString = parseResponseToString(request);
        ResponseDTO<GroupsDTO> responseDTO = null;
        try {
            responseDTO = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<GroupsDTO>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @Override
    void createCustomer() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("");

        customerDTO = new CustomerDTO();
        customerDTO.setUser(userDTO);

        String content = fromObjectToString(customerDTO);
        RequestBuilder request = createRequest(HttpMethod.POST, content, "/api/customers");
        String responseAsString = null;
        ResponseDTO<CustomerDTO> responseDTO = null;
        try {
            responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
            responseDTO = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<CustomerDTO>>() {});
        } catch (Exception e) {
            log.info("Error while sending request to API!");
            throw new RuntimeException(e);
        }

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getSuccess());
        log.info("Message of ResponseDTO: {}", responseDTO.getMessage());
    }

    @Test
    @Order(4)
    @Override
    void createTaskForGroups() {
        RequestBuilder request = createRequest(HttpMethod.POST, null, "/api/tasks/");
        String responseAsString = null;
        try {
            responseAsString =
                mvc.perform(request).andExpect(MockMvcResultMatchers.status().is(400)).andReturn().getResponse().getContentAsString();

            Assertions.assertTrue(responseAsString.contains("Bad Request"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        taskDTO = new TaskDTO();
        taskDTO.setId(7593752345l);

        String content = fromObjectToString(taskDTO);
        request = createRequest(HttpMethod.POST, content, "/api/tasks/");
        ResponseDTO<TaskDTO> responseDTO = null;
        try {
            responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
            responseDTO = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<TaskDTO>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getSuccess());
        log.info("Message of ResponseDTO: {} ", responseDTO.getMessage());
    }

    @Test
    @Order(5)
    @Override
    void getStatusesOfTasks() {
        ResponseDTO<List<CustomerResultDTO>> responseDTO = null;
        String responseAsString = null;
        RequestBuilder request = createRequest(HttpMethod.GET, null, "/api/todo/result/" + 1432124242l);
        try {
            responseAsString =
                mvc.perform(request).andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getContentAsString();
            log.info("Response as String: {}", responseAsString);
            responseDTO = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<List<CustomerResultDTO>>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getSuccess());
        log.info("Message of ResponseDTO: {}", responseDTO.getMessage());
    }

    @Test
    @Order(6)
    @Override
    public void deleteAll() {}

    private RequestBuilder createRequest(HttpMethod method, String content, String uri) {
        try {
            return MockMvcRequestBuilders
                .request(method, new URI(uri))
                .content(content == null ? "" : content)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminKey);
        } catch (URISyntaxException e) {
            log.error("Error while creating URI with String url");
            throw new RuntimeException(e);
        }
    }

    private String fromObjectToString(Object object) {
        try {
            String content = jsonMapper.writeValueAsString(object);
            log.info("DTO object as string: {}", object);
            return content;
        } catch (JsonProcessingException e) {
            log.info("Error while parsing DTO to String");
            throw new RuntimeException(e);
        }
    }

    private String parseResponseToString(RequestBuilder request) {
        try {
            String responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();

            log.info("Response as string: {}", responseAsString);
            return responseAsString;
        } catch (Exception e) {
            log.error("Error while sending request to API");
            throw new RuntimeException(e);
        }
    }

    private String parseResponseToString(RequestBuilder request, Boolean signal) {
        try {
            String responseAsString = mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn()
                .getResponse()
                .getContentAsString();

            log.info("Response as string: {}", responseAsString);
            return responseAsString;
        } catch (Exception e) {
            log.error("Error while sending request to API");
            throw new RuntimeException(e);
        }
    }
}
