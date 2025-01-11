package space.space_spring.global.util.bindingResult;

import org.springframework.validation.BindingResult;

public class BindingResultUtils {

    public static String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(". "));
        return errorMessage.toString();
    }
}
