package space.space_spring.response.status;

import org.springframework.http.HttpStatus;

public interface ResponseStatus {


    int getCode();

    HttpStatus getStatus();

    String getMessage();
}
