package todo.manager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapperTaskImplTest {

    private TaskMapper taskMapper;

    @BeforeEach
    public void setUp() {
        taskMapper = new TaskMapperImpl();
    }
}
