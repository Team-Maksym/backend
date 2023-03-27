package starlight.backend.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("The password is wrong!");
    }
}