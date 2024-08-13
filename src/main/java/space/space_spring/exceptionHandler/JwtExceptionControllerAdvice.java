package space.space_spring.exceptionHandler;

import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import space.space_spring.exception.jwt.bad_request.JwtBadRequestException;
import space.space_spring.exception.jwt.unauthorized.JwtUnauthorizedTokenException;
import space.space_spring.response.BaseErrorResponse;

import java.security.SignatureException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.WRONG_SIGNATURE_JWT;

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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureException.class)
    public BaseErrorResponse handle_JwtSignatureException(SignatureException e) {
        log.error("[handle_JwtUnauthorizedException]", e);
        return new BaseErrorResponse(WRONG_SIGNATURE_JWT);
    }
}
