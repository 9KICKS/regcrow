package africa.semicolon.regcrow.exception;

import africa.semicolon.regcrow.exceptions.RegcrowException;

public class UserNotFoundException extends RegcrowException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
