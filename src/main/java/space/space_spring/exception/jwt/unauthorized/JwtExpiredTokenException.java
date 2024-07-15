package space.space_spring.exception.jwt.unauthorized;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;

@Getter
public class JwtExpiredTokenException extends JwtUnauthorizedTokenException {

    private final ResponseStatus exceptionStatus;

    public JwtExpiredTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }

}
