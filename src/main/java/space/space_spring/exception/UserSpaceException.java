package space.space_spring.exception;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;

@Getter
public class UserSpaceException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public UserSpaceException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public UserSpaceException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
