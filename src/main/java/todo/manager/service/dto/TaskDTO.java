package todo.manager.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link todo.manager.domain.Task} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonPropertyOrder(value = { "id", "body", "image", "imageContentType", "caption", "groups" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("body")
    private String body;

    @Lob
    @JsonProperty("image")
    private byte[] image;

    @JsonProperty("imageContentType")
    private String imageContentType;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("groups")
    private Set<GroupsDTO> groups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Set<GroupsDTO> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupsDTO> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", image='" + getImage() + "'" +
            ", caption='" + getCaption() + "'" +
            ", groups=" + getGroups() +
            "}";
    }
}
