package space.space_spring.global.exception;

import lombok.Getter;
import space.space_spring.global.common.response.status.ResponseStatus;
@Getter
public class InternalServerErrorException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public InternalServerErrorException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
