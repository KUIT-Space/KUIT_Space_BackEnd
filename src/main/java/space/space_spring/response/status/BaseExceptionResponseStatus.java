package space.space_spring.response.status;


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
    TOKEN_NOT_FOUND(4001, HttpStatus.BAD_REQUEST, "토큰이 HTTP Header에 없습니다."),
    UNSUPPORTED_TOKEN_TYPE(4002, HttpStatus.BAD_REQUEST, "지원되지 않는 토큰 형식입니다."),
    INVALID_TOKEN(4003, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(4004, HttpStatus.UNAUTHORIZED, "토큰이 올바르게 구성되지 않았습니다."),
    EXPIRED_TOKEN(4005, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    TOKEN_MISMATCH(4006, HttpStatus.UNAUTHORIZED, "로그인 정보가 토큰 정보와 일치하지 않습니다."),

    /**
     * 5000: User 오류
     */
    INVALID_USER_SIGNUP(5000, HttpStatus.BAD_REQUEST, "회원가입 요청에서 잘못된 값이 존재합니다."),
    DUPLICATE_EMAIL(5001, HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(5002, HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(4003, HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    PASSWORD_NO_MATCH(4004, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_USER_STATUS(4005, HttpStatus.BAD_REQUEST, "잘못된 회원 status 값입니다."),
    EMAIL_NOT_FOUND(4006, HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    INVALID_USER_LOGIN(4007, HttpStatus.BAD_REQUEST, "로그인 요청에서 잘못된 값이 존재합니다."),


    /**
     * 6000: Space 오류
     */
    INVALID_SPACE_CREATE(6000, HttpStatus.BAD_REQUEST, "스페이스 생성 요청에서 잘못된 값이 존재합니다."),
    SPACE_NOT_FOUND(6001, HttpStatus.BAD_REQUEST, "존재하지 않는 스페이스입니다."),
    adff(6002, HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    baab(6003, HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    nff(6004, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    gnf(6005, HttpStatus.BAD_REQUEST, "잘못된 회원 status 값입니다."),
    fb(6006, HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),


    /**
     * 7000: UserSpace 오류
     */

    USER_IS_NOT_IN_SPACE(7000, HttpStatus.BAD_REQUEST, "해당 스페이스에 속하지 않는 유저입니다."),
    UNAUTHORIZED_USER(7001, HttpStatus.UNAUTHORIZED, "해당 스페이스에 관리자 권한이 없는 유저입니다."),
    USER_IS_ALREADY_IN_SPACE(7002, HttpStatus.BAD_REQUEST, "해당 스페이스에 이미 가입되어 있는 유저입니다"),
    D(7003, HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    E(7004, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    F(7005, HttpStatus.BAD_REQUEST, "잘못된 회원 status 값입니다."),
    G(7006, HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),





    /**
     * 8000: Chat 오류
     */
    INVALID_CHATROOM_CREATE(8000, HttpStatus.BAD_REQUEST, "채팅방 생성 요청에서 잘못된 값이 존재합니다."),
    CHATROOM_NOT_EXIST(8001, HttpStatus.BAD_REQUEST, "존재하지 않는 채팅방입니다."),
    INVALID_CHATROOM_JOIN(8001, HttpStatus.BAD_REQUEST, "채팅방 멤버 초대 요청에서 잘못된 값이 존재합니다."),

    /**
     * 9000 : MultipartFile 오류
     */

    IS_NOT_IMAGE_FILE(9000, HttpStatus.BAD_REQUEST, "지원되는 이미지 파일의 형식이 아닙니다."),
    MULTIPARTFILE_CONVERT_FAILE_IN_MEMORY(9001,HttpStatus.INTERNAL_SERVER_ERROR,"multipartFile memory 변환 과정에서 문제가 생겼습니다."),
    /*
     * 10000: voice room 오류
     */
    VOICEROOM_NOT_EXIST(10001, HttpStatus.BAD_REQUEST,"존재하지 않는 보이스룸 id입니다."),
    INVALID_VOICEROOM_REQUEST(10002, HttpStatus.BAD_REQUEST,"잘못된 요청 인자 입니다."),

    VOICEROOM_NAME_ALREADY_EXIST(10003, HttpStatus.BAD_REQUEST,"이미 존재하는 VoiceRoom 이름 입니다."),
    VOICEROOM_NOT_IN_SPACE(10004, HttpStatus.BAD_REQUEST,"이미 존재하는 VoiceRoom 이름 입니다."),
    VOICEROOM_DO_NOT_HAVE_PERMISSION(10005,HttpStatus.FORBIDDEN,"해당 작업은 관리자 권한이 필요합니다." );



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
