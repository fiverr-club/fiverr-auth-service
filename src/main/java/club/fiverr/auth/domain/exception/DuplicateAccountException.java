package club.fiverr.auth.domain.exception;

public class DuplicateAccountException extends AuthDomainException {

    public DuplicateAccountException(String message) {
        super(message);
    }
}
