package starlight.backend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.ProofNotFoundException;
import starlight.backend.exception.TalentAlreadyOccupiedException;
import starlight.backend.exception.TalentNotFoundException;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO notFound(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(TalentAlreadyOccupiedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO alreadyIs(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler({
            ProofNotFoundException.class,
            TalentNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO notExists(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    record ErrorDTO(String message) {
    }
}