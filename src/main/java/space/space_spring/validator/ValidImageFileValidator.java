package space.space_spring.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class ValidImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {

    // multipartFile이 null 이 아니거나 비어있지 않으면 검증로직 통과

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;          // 최대 용량 : 5MB

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        // TODO 1. 이미지 파일이 비어있지 않은지 check
        if (multipartFile == null || multipartFile.isEmpty()) {
            return false;
        }

        // TODO 2. 이미지 파일이 지정한 확장자를 가지는지 check
        String fileName = multipartFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        boolean isValidExtension = Arrays.stream(AllowedImageFileExtensions.values()).anyMatch(
                ext -> ext.getExtension().equals(extension)
        );
        if (!isValidExtension) {
            return false;
        }

        // TODO 3. 이미지 파일이 최대용량보다 작거나 같은지 check
        if (multipartFile.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        return true;
    }

}
