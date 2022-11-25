package todo.manager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link todo.manager.domain.Todo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonPropertyOrder({ "id", "createdAt", "none", "task", "customer" })
@JsonIgnoreProperties(value = { "createdAt" }, ignoreUnknown = true, allowSetters = true)
public class TodoDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    //    @JsonProperty("createdAt")
    private LocalDate createdAt;

    @JsonProperty("none")
    private Boolean none;

    @JsonProperty("task")
    private TaskDTO task;

    @JsonProperty("customer")
    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getNone() {
        return none;
    }

    public void setNone(Boolean none) {
        this.none = none;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoDTO)) {
            return false;
        }

        TodoDTO todoDTO = (TodoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, todoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", none='" + getNone() + "'" +
            ", task=" + getTask() +
            ", customer=" + getCustomer() +
            "}";
    }
}
