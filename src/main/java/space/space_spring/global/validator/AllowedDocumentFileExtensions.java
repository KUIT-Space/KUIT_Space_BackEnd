package space.space_spring.global.validator;

import lombok.Getter;

@Getter
public enum AllowedDocumentFileExtensions {
    /**
     * 허용가능한 이미지 파일 확장자 list
     */
    PDF("pdf"),
    DOCX("docx"),
    HWP("hwp"),
    TXT("txt");

    private String extension;

    AllowedDocumentFileExtensions(String extension) {
        this.extension = extension;
    }

    public static boolean contains(String extension) {
        extension = extension.toLowerCase();        // 입력된 확장자를 소문자로 변환

        for (AllowedDocumentFileExtensions allowedFileExtensions : AllowedDocumentFileExtensions.values()) {
            if (allowedFileExtensions.getExtension().equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
