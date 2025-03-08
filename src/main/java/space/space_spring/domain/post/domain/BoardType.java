package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public enum BoardType {
    POST,   // 일반 게시글
    QUESTION,   // 질문
    TIP,    // tip
    NOTICE,   // 공지사항
    SEASON_NOTICE;  // 기수별 공지사항

    public static BoardType fromString(String stringOfBoardType) {
        try {
            return BoardType.valueOf(stringOfBoardType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 BoardType입니다. 사용 가능한 값: [POST, QUESTION, TIP, NOTICE, SEASON_NOTICE], 입력값: " + stringOfBoardType);
        }
    }
}
