//package todo.manager.handlers;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import todo.manager.service.dto.ResponseDTO;
//import todo.manager.web.rest.errors.GroupsExceptions;
//
//@ControllerAdvice
//public class GroupExceptionHandler implements GroupErrorHandler {
//
//    private Logger log = LoggerFactory.getLogger(GroupExceptionHandler.class);
//
//    @Override
//    @ResponseBody
//    @ExceptionHandler(GroupsExceptions.GroupsFieldNull.class)
//    public ResponseDTO<Object> fieldNull(GroupsExceptions.GroupsFieldNull e){
//        log.error("Field null: {} ", e.getMessage());
//        return new ResponseDTO<>(false, e.getMessage(), null);
//    }
//
//    @Override
//    @ResponseBody
//    @ExceptionHandler(GroupsExceptions.GroupsIdNull.class)
//    public ResponseDTO<Object> idNull(GroupsExceptions.GroupsIdNull e) {
//        log.error("ID null: {} ", e.getMessage());
//        return new ResponseDTO<>(false, e.getMessage(), null);
//    }
//
//    @Override
//    @ResponseBody
//    @ExceptionHandler(GroupsExceptions.GroupIdInvalid.class)
//    public ResponseDTO<Object> idIsInvalid(GroupsExceptions.GroupIdInvalid e) {
//        log.error("ID is invalid: {} ", e.getMessage());
//        return new ResponseDTO<>(false, e.getMessage(), null);
//    }
//
//    @Override
//    @ResponseBody
//    @ExceptionHandler(GroupsExceptions.GroupIsExists.class)
//    public ResponseDTO<Object> groupExists(GroupsExceptions.GroupIsExists e) {
//        log.error("Group exists: {} ", e.getMessage());
//        return new ResponseDTO<>(false, e.getMessage(), null);
//    }
//}
