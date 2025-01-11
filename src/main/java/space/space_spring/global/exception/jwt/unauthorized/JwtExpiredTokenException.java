package space.space_spring.global.exception.jwt.unauthorized;

import lombok.Getter;
import space.space_spring.global.common.response.status.ResponseStatus;

@Getter
public class JwtExpiredTokenException extends JwtUnauthorizedTokenException {

    private final ResponseStatus exceptionStatus;

    public JwtExpiredTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }

}
