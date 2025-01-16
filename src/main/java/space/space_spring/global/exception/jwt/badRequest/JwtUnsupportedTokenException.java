package space.space_spring.global.exception.jwt.badRequest;

import lombok.Getter;
import space.space_spring.global.common.response.status.ResponseStatus;

@Getter
public class JwtUnsupportedTokenException extends JwtBadRequestException {

    private final ResponseStatus exceptionStatus;

    public JwtUnsupportedTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }
}
