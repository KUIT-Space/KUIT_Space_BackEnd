package space.space_spring.exception;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;
@Getter
public class InternalServerErrorException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public InternalServerErrorException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
