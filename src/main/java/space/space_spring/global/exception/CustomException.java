package space.space_spring.global.exception;

import lombok.Getter;
import space.space_spring.global.common.response.status.ResponseStatus;

@Getter
public class CustomException extends RuntimeException{

    private final ResponseStatus exceptionStatus;

    public CustomException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public CustomException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
