package space.space_spring.global.exception.jwt.unauthorized;

import lombok.Getter;
import space.space_spring.global.common.response.status.ResponseStatus;

@Getter
public class JwtMalformedTokenException extends JwtUnauthorizedTokenException {

    private final ResponseStatus exceptionStatus;

    public JwtMalformedTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }
}
