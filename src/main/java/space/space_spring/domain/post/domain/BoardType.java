package space.space_spring.domain.post.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BoardType {
    POST("게시판","익명 기능이 없습니다."),
    QUESTION("질문-게시판","익명으로 질문을 작성할 수 있습니다."),
    PAY("정산-게시판","정산 생성 시 알림을 보낼 게시판입니다. 딱 한 개만 설정 가능합니다."),
    TIP("팁-게시판","팁 게시판입니다."),    // tip
    NOTICE("공지사항","전체 공지사항 게시판입니다."),   // 공지사항
    SEASON_NOTICE("기수-공지사항","기수 공지사항 입니다.");  // 기수별 공지사항;

    private String krName;
    private String detail;

    private BoardType(String krName,String detail){
        this.krName=krName;
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
