package todo.manager.web.rest.errors;

public class TodoExceptions {

    public static TodoExists alreadyExists() {
        return new TodoExists();
    }

    static class TodoExists extends RuntimeException {

        public TodoExists() {
            super("Already exists!");
        }
    }
}
