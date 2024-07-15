package space.space_spring.exception.jwt.bad_request;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;

@Getter
public class JwtBadRequestException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public JwtBadRequestException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
