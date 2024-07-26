package space.space_spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.dto.oAuthInfo.KakaoInfo;
import space.space_spring.entity.User;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.OAuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;

    @Value("${oauth.kakao.client.id}")
    private String clientId;

    @Value("${oauth.kakao.redirect.uri}")
    private String redirectUri;

    @Value("${oauth.kakao.client.secret}")
    private String clientSecret;

    /**
     * 유저가 카카오 로그인 동의 시 호출될 콜백 함수
     */
    @GetMapping("/callback/kakao")
    public BaseResponse<String> kakaoCallback(@RequestParam(name = "code") String code, HttpServletResponse response) {

        // TODO 1. 인가코드 받기
        // 카카오 인증 서버는 서비스 서버의 Redirect URI로 인가 코드를 전달함
        log.info("인가 코드 = {}", code);

        // TODO 2. 인가코드를 기반으로 토큰(Access Token) 발급
        String accessToken = null;
        try {
            accessToken = oAuthService.getAccessToken(code, clientId, redirectUri, clientSecret);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("accessToken = {}", accessToken);

        // TODO 3. 엑세스 토큰를 통해 유저 정보 조회
        KakaoInfo kakaoInfo = null;
        try {
            kakaoInfo = oAuthService.getKakaoInfo(accessToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("kakaoInfo.getEmail = {}", kakaoInfo.getEmail());
        log.info("kakaoInfo.getNickname = {}", kakaoInfo.getNickName());

        // TODO 4. 카카오 사용자 정보 확인
        // 유저 email 정보가 db에 없을 시 -> 회원가입 & 로그인
        // 유저 email 정보가 db에 있을 시 -> 로그인
        User userByOAuthInfo = oAuthService.findUserByOAuthInfo(kakaoInfo);

        // TODO 5. 카카오 로그인 유저에게 jwt 발급
        String jwtOAuthLogin = oAuthService.provideJwtToOAuthUser(userByOAuthInfo);
        response.setHeader("Authorization", "Bearer " + jwtOAuthLogin);
        log.info("jwtOAuthLogin = {}", jwtOAuthLogin);

        return new BaseResponse<>("카카오 로그인 성공");
    }
}
