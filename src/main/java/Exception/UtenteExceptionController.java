package Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UtenteExceptionController {
    @ExceptionHandler(value= UtenteBadTypeException.class)
    public ResponseEntity<Object> exception(UtenteBadTypeException exception){
        return new ResponseEntity<>("Incorrect User Type", HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value= UtenteAlreadyInException.class)
    public ResponseEntity<Object> exception(UtenteAlreadyInException exception){
        return new ResponseEntity<>("User already in", HttpStatus.FOUND);
    }
    @ExceptionHandler(value= UtenteNotExistException.class)
    public ResponseEntity<Object> exception(UtenteNotExistException exception){
        return new ResponseEntity<>("User not in", HttpStatus.NOT_FOUND);
    }
}
