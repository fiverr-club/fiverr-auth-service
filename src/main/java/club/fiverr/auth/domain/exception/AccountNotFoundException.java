package club.fiverr.auth.domain.exception;

public class AccountNotFoundException extends AuthDomainException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
