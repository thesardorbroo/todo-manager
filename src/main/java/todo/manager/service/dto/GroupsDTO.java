package todo.manager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link todo.manager.domain.Groups} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupsDTO implements Serializable {

    private Long id;

    private String groupName;

    private Set<TaskDTO> tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupsDTO)) {
            return false;
        }

        GroupsDTO groupsDTO = (GroupsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupsDTO{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
