package space.space_spring.global.exceptionHandler;

import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import space.space_spring.global.exception.jwt.badRequest.JwtBadRequestException;
import space.space_spring.global.exception.jwt.unauthorized.JwtUnauthorizedTokenException;
import space.space_spring.global.common.response.BaseErrorResponse;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class JwtExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtBadRequestException.class)
    public BaseErrorResponse handle_JwtBadRequestException(JwtBadRequestException e) {
        log.error("[handle_JwtBadRequestException]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtUnauthorizedTokenException.class)
    public BaseErrorResponse handle_JwtUnauthorizedException(JwtUnauthorizedTokenException e) {
        log.error("[handle_JwtUnauthorizedException]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }
}
