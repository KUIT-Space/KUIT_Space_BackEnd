package space.space_spring.global.common.response.status;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus {

    /**
     * 1000: 요청 성공 (OK)
     */
    SUCCESS(1000, HttpStatus.OK, "요청에 성공하였습니다."),

    /**
     * 2000: Request 오류 (BAD_REQUEST)
     */
    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(2001, HttpStatus.BAD_REQUEST, "유효하지 않은 URL 입니다."),
    METHOD_NOT_ALLOWED(2002, HttpStatus.METHOD_NOT_ALLOWED, "해당 URL에서는 지원하지 않는 HTTP Method 입니다."),
    HTTP_MESSAGE_NOT_READABLE(2003, HttpStatus.BAD_REQUEST,"request body 양식에 문제가 있습니다"),
    SPACE_ID_PATHVARIABLE_ERROR(2004,HttpStatus.BAD_REQUEST,"URL에 포함된 SPACE_ID 값이 잘못되었습니다."),
    SPACE_ID_PATHVARIABLE_NULL(2005,HttpStatus.BAD_REQUEST,"URL에 포함된 SPACE_ID 값이 없습니다"),


    /**
     * 3000: Server, Database 오류 (INTERNAL_SERVER_ERROR)
     */
    SERVER_ERROR(3000, HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생하였습니다."),
    DATABASE_ERROR(3001, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스에서 오류가 발생하였습니다."),
    BAD_SQL_GRAMMAR(3002, HttpStatus.INTERNAL_SERVER_ERROR, "SQL에 오류가 있습니다."),

    /**
     * 4000: Authorization 오류
     */
    JWT_ERROR(4000, HttpStatus.UNAUTHORIZED, "JWT에서 오류가 발생하였습니다."),
    TOKEN_NOT_FOUND(4001, HttpStatus.UNAUTHORIZED, "토큰이 HTTP Header에 없습니다."),
    UNSUPPORTED_TOKEN_TYPE(4002, HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰 형식입니다."),
    INVALID_TOKEN(4003, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(4004, HttpStatus.UNAUTHORIZED, "토큰이 올바르게 구성되지 않았습니다."),
    EXPIRED_ACCESS_TOKEN(4005, HttpStatus.UNAUTHORIZED, "만료된 access token 입니다."),
    TOKEN_MISMATCH(4006, HttpStatus.UNAUTHORIZED, "저장된 refresh token 과 전달받은 refresh token 이 일치하지 않습니다. 다시 로그인해야합니다."),
    CANNOT_FIND_USER_ID(4007, HttpStatus.UNAUTHORIZED,"토큰의 userId정보를 찾을 수 없습니다."),
    WRONG_SIGNATURE_JWT(4008,HttpStatus.UNAUTHORIZED,"JWT 서명이 잘못 되었습니다."),
    EXPIRED_REFRESH_TOKEN(4009, HttpStatus.UNAUTHORIZED, "만료된 refresh token 입니다. 다시 로그인해야합니다."),
    DISCORD_TOKEN_ERROR(4010, HttpStatus.UNAUTHORIZED, "디스코드 서버에서 access token 발급이 실패하였습니다."),
    CANNOT_FIND_DISCORD_USER(4011, HttpStatus.NOT_FOUND, "디스코드 계정의 정보를 가져오는 데에 실패하였습니다."),

    /**
     * 5000: User 오류
     */
    INVALID_USER_SIGNUP(5000, HttpStatus.BAD_REQUEST, "회원가입 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_EMAIL(5001, HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(5002, HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(4003, HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    PASSWORD_NO_MATCH(4004, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_USER_STATUS(4005, HttpStatus.BAD_REQUEST, "잘못된 회원 status 값입니다."),
    EMAIL_NOT_FOUND(4006, HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    INVALID_USER_LOGIN(4007, HttpStatus.BAD_REQUEST, "로그인 요청에서 잘못된 값이 존재합니다."),

    /**
     * 6000: Space 오류
     */
    INVALID_SPACE_CREATE(6000, HttpStatus.BAD_REQUEST, "스페이스 생성 요청에서 잘못된 값이 존재합니다."),
    SPACE_NOT_FOUND(6001, HttpStatus.NOT_FOUND, "존재하지 않는 스페이스입니다."),
    INVALID_USER_SPACE_PROFILE(6002, HttpStatus.BAD_REQUEST, "스페이스 별 유저 프로필 정보 수정 요청에서 잘못된 값이 존재합니다."),
    INVALID_SPACE_JOIN_REQUEST(6003, HttpStatus.BAD_REQUEST, "스페이스 가입 요청에서 잘못된 값이 존재합니다."),

    nff(6004, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    gnf(6005, HttpStatus.BAD_REQUEST, "잘못된 회원 status 값입니다."),
    fb(6006, HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    SPACE_ALREADY_EXISTED(6007,HttpStatus.BAD_REQUEST,"이미 등록된 SPACE입니다"),

    /**
     * 7000: SpaceMember 오류
     */
    USER_IS_NOT_IN_SPACE(7000, HttpStatus.NOT_FOUND, "해당 스페이스에 속하지 않는 유저입니다."),
    UNAUTHORIZED_USER(7001, HttpStatus.FORBIDDEN, "해당 스페이스에 관리자 권한이 없는 유저입니다."),
    USER_IS_ALREADY_IN_SPACE(7002, HttpStatus.BAD_REQUEST, "해당 스페이스에 이미 가입되어 있는 유저입니다"),
    SPACE_MEMBER_NOT_FOUND(7003, HttpStatus.NOT_FOUND, "존재하지 않는 스페이스 멤버입니다."),
    E(7004, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    F(7005, HttpStatus.BAD_REQUEST, "잘못된 회원 status 값입니다."),
    G(7006, HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),

    BASE64_CONVERT_FAIL_IN_MEMORY(8002, HttpStatus.INTERNAL_SERVER_ERROR, "base64 파일 변환 과정에서 문제가 생겼습니다."),

    /**
     * 9000 : MultipartFile 오류
     */

    IS_NOT_IMAGE_FILE(9000, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원되는 이미지 파일의 형식이 아닙니다."),
    MULTIPARTFILE_CONVERT_FAIL_IN_MEMORY(9001,HttpStatus.INTERNAL_SERVER_ERROR,"multipartFile memory 변환 과정에서 문제가 생겼습니다."),

    /**
     * 11000: Post 오류
     */
    INVALID_BOARD_CREATE(11000, HttpStatus.BAD_REQUEST, "게시판 생성 요청에서 잘못된 값이 존재합니다."),

    INVALID_POST_CREATE(11000, HttpStatus.BAD_REQUEST, "게시글 생성 요청에서 잘못된 값이 존재합니다."),
    POST_NOT_EXIST(11001, HttpStatus.NOT_FOUND, "존재하지 않는 게시글 id입니다."),
    POST_IS_NOT_IN_SPACE(11002, HttpStatus.NOT_FOUND, "해당 게시글은 이 스페이스에 속하지 않습니다."),
    ALREADY_LIKED_THE_POST(11003, HttpStatus.BAD_REQUEST, "해당 게시글에 이미 좋아요를 눌렀습니다."),
    NOT_LIKED_THE_POST_YET(11003, HttpStatus.BAD_REQUEST, "유저가 해당 게시글에 좋아요를 누르지 않았습니다."),
    COMMENT_NOT_EXIST(11004, HttpStatus.NOT_FOUND, "존재하지 않는 댓글 id입니다."),
    ALREADY_LIKED_THE_COMMENT(11005, HttpStatus.BAD_REQUEST, "해당 댓글에 이미 좋아요를 눌렀습니다."),
    NOT_LIKED_THE_COMMENT_YET(11006, HttpStatus.BAD_REQUEST, "유저가 해당 댓글에 좋아요를 누르지 않았습니다."),
    TARGET_ID_MISSING(11007, HttpStatus.BAD_REQUEST, "대댓글 작성 시 targetId가 필요합니다"),
    INVALID_TARGET_ID(11008, HttpStatus.BAD_REQUEST, "댓글 작성 시 targetId는 허용되지 않습니다"),
    COMMENT_IS_NOT_IN_POST(11009, HttpStatus.NOT_FOUND, "해당 댓글이 이 게시글에 속하지 않습니다."),

    /**
     * 12000 : Pay 오류
     */
    INVALID_PAY_CREATE(12000, HttpStatus.BAD_REQUEST, "정산 생성 요청에서 잘못된 값이 존재합니다."),
    PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE(12001, HttpStatus.BAD_REQUEST, "정산 생성자와 정산 요청 대상자가 같은 스페이스에 속해 있지 않습니다. 정산 요청은 같은 스페이스 멤버에게만 가능합니다."),
    INVALID_INDIVIDUAL_AMOUNT(12003, HttpStatus.BAD_REQUEST, "정산 요청 금액들의 합과 정산 요청 총 금액이 일치하지 않습니다."),
    INVALID_EQUAL_SPLIT_AMOUNT(12004, HttpStatus.BAD_REQUEST, "정산 요청 금액들 중 1/N 정산 정책에 위배되는 값이 있습니다."),
    PAY_REQUEST_NOT_FOUND(12005, HttpStatus.NOT_FOUND, "존재하지 않는 정산입니다."),
    PAY_REQUEST_TARGET_NOT_FOUND(12006, HttpStatus.NOT_FOUND, "존재하지 않는 정산요청타겟 입니다"),
    THIS_PAY_REQUEST_HAS_NOT_TARGETS(12007, HttpStatus.INTERNAL_SERVER_ERROR, "현재 정산 요청은 정산 요청 대상이 없습니다. 현재 정산 생성 시 서버에 문제가 있었습니다."),
    INVALID_PAY_REQUEST_TARGET_ID(12008, HttpStatus.BAD_REQUEST, "정산 요청 타겟 id의 타겟 멤버가 본인과 일치하지 않습니다. 본인의 정산에 대해서만 완료처리를 할 수 있습니다.");
  
    private final int code;
    private final HttpStatus status;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
