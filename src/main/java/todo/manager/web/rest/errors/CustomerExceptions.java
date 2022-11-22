package todo.manager.web.rest.errors;

public class CustomerExceptions {

    public static IdNull idNull() {
        return new IdNull();
    }

    static class IdNull extends RuntimeException {

        public IdNull() {
            super("Customer's id is null");
        }
    }
}
