package space.space_spring.global.exception;

import lombok.Getter;
import space.space_spring.global.common.response.status.ResponseStatus;

@Getter
public class BadRequestException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public BadRequestException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.exceptionStatus = responseStatus;
    }
}
