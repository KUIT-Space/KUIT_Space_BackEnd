package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public enum AttachmentType {
    IMAGE,
    FILE;

    public static AttachmentType fromString(String stringOfAttachmentType) {
        try {
            return AttachmentType.valueOf(stringOfAttachmentType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 AttachmentType입니다. 사용 가능한 값: [IMAGE, FILE], 입력값: " + stringOfAttachmentType);
        }
    }
}
