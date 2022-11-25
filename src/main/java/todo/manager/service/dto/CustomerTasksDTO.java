package todo.manager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder(value = { "completedTasks", "notCompleted" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerTasksDTO {

    @JsonProperty("completedTasks")
    private List<TaskDTO> completedTasks;

    @JsonProperty("notCompleted")
    private List<TaskDTO> notCompleted;

    public CustomerTasksDTO() {}

    public CustomerTasksDTO(List<TaskDTO> completedTasks, List<TaskDTO> notCompleted) {
        this.completedTasks = completedTasks;
        this.notCompleted = notCompleted;
    }

    public List<TaskDTO> getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(List<TaskDTO> completedTasks) {
        this.completedTasks = completedTasks;
    }

    public List<TaskDTO> getNotCompleted() {
        return notCompleted;
    }

    public void setNotCompleted(List<TaskDTO> notCompleted) {
        this.notCompleted = notCompleted;
    }
}
