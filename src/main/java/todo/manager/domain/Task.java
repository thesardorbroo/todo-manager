package todo.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "body")
    private String body;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "caption")
    private String caption;

    @OneToMany(mappedBy = "task")
    @JsonIgnoreProperties(value = { "task" }, allowSetters = true)
    private Set<Comments> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_task__groups", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "groups_id"))
    //    @JsonIgnoreProperties(value = { "tasks" }, allowSetters = true)
    private Set<Groups> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return this.body;
    }

    public Task body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Task image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Task imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getCaption() {
        return this.caption;
    }

    public Task caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Set<Comments> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comments> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setTask(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setTask(this));
        }
        this.comments = comments;
    }

    public Task comments(Set<Comments> comments) {
        this.setComments(comments);
        return this;
    }

    public Task addComments(Comments comments) {
        this.comments.add(comments);
        comments.setTask(this);
        return this;
    }

    public Task removeComments(Comments comments) {
        this.comments.remove(comments);
        comments.setTask(null);
        return this;
    }

    public Set<Groups> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

    public Task groups(Set<Groups> groups) {
        this.setGroups(groups);
        return this;
    }

    public Task addGroups(Groups groups) {
        this.groups.add(groups);
        groups.getTasks().add(this);
        return this;
    }

    public Task removeGroups(Groups groups) {
        this.groups.remove(groups);
        groups.getTasks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", caption='" + getCaption() + "'" +
            "}";
    }
}
