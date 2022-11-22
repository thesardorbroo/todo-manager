package todo.manager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link todo.manager.domain.Customer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonPropertyOrder(value = { "id", "user", "group" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user")
    private UserDTO user;

    @JsonProperty("group")
    private GroupsDTO group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public GroupsDTO getGroup() {
        return group;
    }

    public void setGroup(GroupsDTO group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            ", group=" + getGroup() +
            "}";
    }
}
