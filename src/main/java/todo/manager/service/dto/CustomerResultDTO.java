package todo.manager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder(value = { "customer", "tasks", "done", "tasksCount", "percent" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerResultDTO {

    @JsonProperty("customer")
    private CustomerDTO customer;

    @JsonProperty("tasks")
    private List<TaskResultDTO> tasks;

    @JsonProperty("done")
    private Integer done;

    @JsonProperty("tasksCount")
    private Integer tasksCount;

    @JsonProperty("percent")
    private Double percent;

    public CustomerResultDTO() {}

    public CustomerResultDTO(CustomerDTO customer, List<TaskResultDTO> tasks, Integer done, Integer tasksCount, Double percent) {
        this.customer = customer;
        this.tasks = tasks;
        this.done = done;
        this.tasksCount = tasksCount;
        this.percent = percent;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<TaskResultDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskResultDTO> tasks) {
        this.tasks = tasks;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public Integer getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(Integer tasksCount) {
        this.tasksCount = tasksCount;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
