package space.space_spring.domain.user.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.user.application.port.in.OauthUseCase;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "인증/인가 관련 API")
public class OauthController {

    private final OauthUseCase oauthUseCase;

    private final static String ACCESS_TOKEN_HEADER = "Authorization";
    private final static String REFRESH_TOKEN_HEADER = "Authorization-refresh";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Operation(summary = "디스코드 OAuth", description = """
        
        디스코드 OAuth 로그인을 수행합니다.
        
        이때, 미리 초기화해놓은 서버에 유저가 없다면 success false를 반환합니다.
        
        """)
    @GetMapping("/oauth/discord")
    public BaseResponse<SuccessResponse> signInDiscord(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        TokenPair tokenPair = oauthUseCase.signIn(code);

        if (tokenPair == null) {
            return new BaseResponse<>(new SuccessResponse(false));
        }

        response.setHeader(ACCESS_TOKEN_HEADER, TOKEN_PREFIX + tokenPair.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, TOKEN_PREFIX + tokenPair.getRefreshToken());
        return new BaseResponse<>(new SuccessResponse(true));
    }

}
