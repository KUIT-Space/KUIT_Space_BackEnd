package space.space_spring.exception;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;

@Getter
public class BadRequestException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public BadRequestException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.exceptionStatus = responseStatus;
    }
}
