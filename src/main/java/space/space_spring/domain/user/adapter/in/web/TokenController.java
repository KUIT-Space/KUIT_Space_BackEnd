package space.space_spring.domain.user.adapter.in.web;

import static space.space_spring.domain.user.adapter.in.web.TokenParser.parseTokenPair;

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
@RequestMapping("/jwt")
public class TokenController {

    private final TokenUseCase tokenUseCase;

    private final static String ACCESS_TOKEN_HEADER = "Authorization";
    private final static String REFRESH_TOKEN_HEADER = "Authorization-refresh";
    private final static String TOKEN_PREFIX = "Bearer ";

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
