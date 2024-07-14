package space.space_spring.exception;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;

@Getter
public class UserException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public UserException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public UserException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
