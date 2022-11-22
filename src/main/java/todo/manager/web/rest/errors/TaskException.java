package todo.manager.web.rest.errors;

public class TaskException {

    public static TaskNotFound notFound() {
        return new TaskNotFound();
    }

    static class TaskNotFound extends RuntimeException {

        public TaskNotFound() {
            super("Task is not found!");
        }
    }
}
