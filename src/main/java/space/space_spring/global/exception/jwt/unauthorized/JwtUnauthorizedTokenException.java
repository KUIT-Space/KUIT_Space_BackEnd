package space.space_spring.global.exception.jwt.unauthorized;

import lombok.Getter;
import space.space_spring.global.common.response.status.ResponseStatus;

@Getter
public class JwtUnauthorizedTokenException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public JwtUnauthorizedTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}



