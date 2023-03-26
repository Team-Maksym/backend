package starlight.backend.talent.controller.advac;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.exception.WrongPasswordException;
import starlight.backend.exception.WrongTokenException;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({
            TalentNotFoundException.class,
            WrongTokenException.class,
            PageNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO purchasedTicketError(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO wrongPasswordError(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    record ErrorDTO(String message) {
    }
}