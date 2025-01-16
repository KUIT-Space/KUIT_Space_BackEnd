package space.space_spring.global.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorConstraint.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValidator {

    /**
     * 특정 enum 클래스의 값만 허용하도록 검증하는 커스텀 어노테이션
     */

    Class<? extends Enum<?>> enumClass();

    String message() default "값이 올바르지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
