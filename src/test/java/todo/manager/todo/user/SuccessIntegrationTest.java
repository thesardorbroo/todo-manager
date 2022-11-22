package todo.manager.todo.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import todo.manager.IntegrationTest;
import todo.manager.service.dto.CustomerTasksDTO;
import todo.manager.service.dto.ResponseDTO;

@IntegrationTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@PropertySource("classpath:config/application-dev.yml")
public class SuccessIntegrationTest extends UserBasicActions {

    @Autowired
    private MockMvc mvc;

    @Override
    void getAllTasks() {
        RequestBuilder request = createRequest(HttpMethod.GET, null, "/api/todo/tasks/");
        ResponseDTO<CustomerTasksDTO> responseDTO = null;
        String responseAsString = null;
        try {
            responseAsString = mvc.perform(request).andReturn().getResponse().getContentAsString();
            log.info("Response as String: {}", responseAsString);
            responseDTO = jsonMapper.readValue(responseAsString, new TypeReference<ResponseDTO<CustomerTasksDTO>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.getSuccess());
        Assertions.assertNotNull(responseDTO.getData());
        log.info("Customer's tasks: {} ", responseDTO.getData());
    }

    @Override
    void markTasks() {}

    @Override
    void useGetAllTasksAgain() {}

    private RequestBuilder createRequest(HttpMethod method, String content, String uri) {
        try {
            return MockMvcRequestBuilders
                .request(method, new URI(uri))
                .content(content == null ? "" : content)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userKey);
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
}
