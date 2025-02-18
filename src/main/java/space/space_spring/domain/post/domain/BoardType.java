package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public enum BoardType {
    POST,
    QUESTION,
    COMMENT;

    public static BoardType fromString(String stringOfBoardType) {
        try {
            return BoardType.valueOf(stringOfBoardType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 BoardType입니다. 사용 가능한 값: [POST, QUESTION, COMMENT], 입력값: " + stringOfBoardType);
        }
    }
}
