package todo.manager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link todo.manager.domain.Comments} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentsDTO implements Serializable {

    private Long id;

    private String message;

    private TaskDTO task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentsDTO)) {
            return false;
        }

        CommentsDTO commentsDTO = (CommentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentsDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", task=" + getTask() +
            "}";
    }
}
