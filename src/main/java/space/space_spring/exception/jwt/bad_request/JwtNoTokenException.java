package space.space_spring.exception.jwt.bad_request;

import lombok.Getter;
import space.space_spring.response.status.ResponseStatus;

@Getter
public class JwtNoTokenException extends JwtBadRequestException {

    private final ResponseStatus exceptionStatus;


    public JwtNoTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }
}
