package space.space_spring.validator;

import lombok.Getter;

@Getter
public enum AllowedImageFileExtensions {
    /**
     * 허용가능한 이미지 파일 확장자 list
     */
    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png"),
    GIF("gif"),
    WebP("webp"),
    SVG("svg"),
    BMP("bmp"),
    TIF("tif"),
    TIFF("tiff"),
    HEIC("heic");

    private String extension;

    AllowedImageFileExtensions(String extension) {
        this.extension = extension;
    }
}
