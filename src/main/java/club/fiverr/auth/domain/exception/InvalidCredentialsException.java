package club.fiverr.auth.domain.exception;

public class InvalidCredentialsException extends AuthDomainException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
