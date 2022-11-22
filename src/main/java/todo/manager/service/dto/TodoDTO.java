package todo.manager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;
import java.util.Set;

@JsonPropertyOrder(value = { "id", "customer", "task", "createdAt" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("customer")
    private CustomerDTO customer;

    @JsonProperty("task")
    private TaskDTO task;

    @JsonProperty("createdAt")
    private Date createdAt;

    public TodoDTO() {}

    public TodoDTO(Integer id, CustomerDTO customer, TaskDTO task, Date createdAt) {
        this.id = id;
        this.customer = customer;
        this.task = task;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
