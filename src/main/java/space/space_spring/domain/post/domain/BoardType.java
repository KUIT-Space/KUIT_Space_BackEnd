package space.space_spring.domain.post.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BoardType {
    POST("게시판","익명 기능이 없습니다."),
    QUESTION("질문-게시판","익명으로 질문을 작성할 수 있습니다."),
    COMMENT("","");

    private String krName;
    private String detail;

    private BoardType(String name,String detail){
        this.krName=name;
        this.detail=detail;
    }

    public static BoardType fromString(String stringOfBoardType) {
        try {
            return BoardType.valueOf(stringOfBoardType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 BoardType입니다. 사용 가능한 값: ["+ Arrays.toString(values())+"], 입력값: " + stringOfBoardType);
        }
    }

    public String getKrName(){
        return this.krName;
    }
    public String getDetail(){
        return this.detail;
    }
    public String getName(){
        return name();
    }
}
