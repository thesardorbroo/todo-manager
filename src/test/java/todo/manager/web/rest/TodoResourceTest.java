package todo.manager.web.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import todo.manager.IntegrationTest;
import todo.manager.domain.Todo;
import todo.manager.repository.TodoRepository;

@IntegrationTest
@AutoConfigureMockMvc
class TodoResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    Todo createDummyTodo() {
        Todo todo = new Todo();
        todo.setCreatedAt(Date.from(Instant.now()));
        return todoRepository.save(todo);
    }

    @Test
    void markTaskToSuccess() throws Exception {
        Todo dummyTodo = createDummyTodo();
        ResultActions resultActions = mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/todo/mark").contentType(MediaType.APPLICATION_JSON).content(dummyTodo.getId().toString())
            )
            .andExpect(status().isOk());

        System.out.println("RESULT: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultActions));
    }

    @Test
    void getTask() {}

    @Test
    void getResult() {}
}
