package space.space_spring.domain.user.adapter.in.web;

import static space.space_spring.domain.user.adapter.in.web.TokenParser.parseTokenPair;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.user.application.port.in.TokenUseCase;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/jwt")
@Tag(name = "OAuth", description = "인증/인가 관련 API")
public class TokenController {

    private final TokenUseCase tokenUseCase;

    private final static String ACCESS_TOKEN_HEADER = "Authorization";
    private final static String REFRESH_TOKEN_HEADER = "Authorization-refresh";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Operation(summary = "access token 재발급", description = """
        
        access token이 만료된 경우 refresh token을 통해 access token을 재발급받습니다.
        
        이때, refresh token도 같이 재발급됩니다.
        
        """)
    @PostMapping("/new-token")
    public BaseResponse<SuccessResponse> updateAccessToken(HttpServletRequest request, HttpServletResponse response) {
        TokenPair newTokenPair = tokenUseCase.updateTokenPair(parseTokenPair(request));

        response.setHeader(ACCESS_TOKEN_HEADER, TOKEN_PREFIX + newTokenPair.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, TOKEN_PREFIX + newTokenPair.getRefreshToken());

        log.info("갱신된 access token : " + newTokenPair.getAccessToken());
        log.info("갱신된 refresh token : " + newTokenPair.getRefreshToken());

        // return
        return new BaseResponse<>(new SuccessResponse(true));
    }
}
