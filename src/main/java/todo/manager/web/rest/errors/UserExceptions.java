package todo.manager.web.rest.errors;

public class UserExceptions {

    public static UserIdInvalid invalidId() {
        return new UserIdInvalid();
    }

    public static UserFieldNull userNull() {
        return new UserFieldNull();
    }

    public static UserIdNull idNull() {
        return new UserIdNull();
    }

    static class UserIdInvalid extends RuntimeException {

        public UserIdInvalid() {
            super("User is not found!");
        }
    }

    static class UserFieldNull extends RuntimeException {

        public UserFieldNull() {
            super("User field is null!");
        }
    }

    static class UserIdNull extends RuntimeException {

        public UserIdNull() {
            super("User's is null");
        }
    }
}
