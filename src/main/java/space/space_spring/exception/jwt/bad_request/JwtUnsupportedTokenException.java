package space.space_spring.exception.jwt.bad_request;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;

@Getter
public class JwtUnsupportedTokenException extends JwtBadRequestException {

    private final ResponseStatus exceptionStatus;

    public JwtUnsupportedTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }
}
