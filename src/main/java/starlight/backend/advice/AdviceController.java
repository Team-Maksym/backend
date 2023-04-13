package starlight.backend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import starlight.backend.exception.*;

import java.sql.SQLException;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({
            PageNotFoundException.class,
            UserCanNotEditProofNotInDraftException.class,
            UserAccesDeniedToProofException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO badRequest(Exception exception) {
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
    @ExceptionHandler({
            Exception.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO exception(Exception e) {
        if (e instanceof SQLException) {
            if (!ignoreSQLException(
                    ((SQLException) e).
                            getSQLState())) {

                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        ((SQLException) e).getSQLState());

                System.err.println("Error Code: " +
                        ((SQLException) e).getErrorCode());

                System.err.println("Message: " + e.getMessage());

                Throwable t = e.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
        return new ErrorDTO(e.getMessage());
    }

    public static boolean ignoreSQLException(String sqlState) {

        if (sqlState == null) {
            System.out.println("The SQL state is not defined!");
            return false;
        }

        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
            return true;

        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
            return true;

        return false;
    }

    record ErrorDTO(String message) {
    }
}