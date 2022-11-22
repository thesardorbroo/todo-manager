package todo.manager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "task", "result" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskResultDTO {

    @JsonProperty("task")
    private TaskDTO task;

    @JsonProperty("result")
    private Boolean result;

    public TaskResultDTO() {}

    public TaskResultDTO(TaskDTO task, Boolean result) {
        this.task = task;
        this.result = result;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
