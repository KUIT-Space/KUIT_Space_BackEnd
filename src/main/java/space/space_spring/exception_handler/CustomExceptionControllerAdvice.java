package space.space_spring.exception_handler;

import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import space.space_spring.exception.CustomException;
import space.space_spring.response.BaseErrorResponse;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class CustomExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseErrorResponse> handle_JwtBadRequestException(CustomException e) {
        log.error("[handle_JwtBadRequestException]", e);
        BaseErrorResponse errorResponse = new BaseErrorResponse(e.getExceptionStatus());
        return ResponseEntity.status(e.getExceptionStatus().getStatus()).body(errorResponse);
    }
}
