package todo.manager.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntities extends ResponseEntity {

    private static Logger log = LoggerFactory.getLogger(ResponseEntities.class);

    public ResponseEntities(HttpStatus status) {
        super(status);
    }

    public ResponseEntities() {
        super(HttpStatus.OK);
    }

    public static ResponseEntity ok(Object entity) {
        log.info("Object's toString(): {} ", entity.toString());
        return ResponseEntity.ok(entity);
    }
}
