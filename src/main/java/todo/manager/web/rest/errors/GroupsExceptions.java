package todo.manager.web.rest.errors;

public class GroupsExceptions {

    public static class GroupsFieldNull extends RuntimeException {

        public GroupsFieldNull() {
            super("Field groups is null!");
        }
    }

    public static class GroupsIdNull extends RuntimeException {

        public GroupsIdNull() {
            super("Group id is null!");
        }
    }

    public static class GroupIdInvalid extends RuntimeException {

        public GroupIdInvalid() {
            super("Group is not found, id is invalid!");
        }
    }

    public static class GroupIsExists extends RuntimeException {

        public GroupIsExists() {
            super("Group is already exists!");
        }
    }

    public static class GroupIdIsBelowZero extends RuntimeException {

        public GroupIdIsBelowZero() {
            super("Group ID is equals to zero or below of zero!");
        }
    }
}
