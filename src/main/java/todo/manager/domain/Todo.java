package todo.manager.domain;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "todo_list")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Task task;

    private Date createdAt;

    public Todo() {}

    public Todo(Integer id, Customer customer, Task task, Date createdAt) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
