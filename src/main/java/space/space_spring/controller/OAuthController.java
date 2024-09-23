package space.space_spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import space.space_spring.dto.jwt.TokenPairDTO;
import space.space_spring.dto.oAuth.KakaoInfo;
import space.space_spring.entity.User;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.OAuthService;

import java.io.IOException;

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
    public void kakaoCallback(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {

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
        TokenPairDTO tokenPairDTO = oAuthService.provideJwtToOAuthUser(userByOAuthInfo);

        // TODO 6. 카카오 로그인 유저에게 발급한 refresh token을 db에 저장
        oAuthService.updateRefreshToken(userByOAuthInfo, tokenPairDTO.getRefreshToken());

        System.out.println("tokenPairDTO.getAccessToken() = " + tokenPairDTO.getAccessToken());
        System.out.println("tokenPairDTO.getRefreshToken() = " + tokenPairDTO.getRefreshToken());

        // 클라이언트로 response 전달
        // -> 메서드 분리 ??
        // 공백문자가 %20 으로 전달되는 듯 함 -> 프론트 분들과 협의 필요할 듯
        String redirectUrl = String.format(
                "https://kuit-space.github.io/KUIT-Space-front/login?access-token=%s&refresh-token=%s&userId=%s",
                "Bearer " + tokenPairDTO.getAccessToken(),
                "Bearer " + tokenPairDTO.getRefreshToken(),
                userByOAuthInfo.getUserId()
        );

        response.sendRedirect(redirectUrl);
    }

    /**
     * 엑세스 토큰 갱신 요청 처리
     * -> 엑세스 토큰, 리프레시 토큰 갱신 (RTR 패턴)
     */
    @PostMapping("/new-token")
    public BaseResponse<String> updateAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // access token, refresh token 파싱
        TokenPairDTO tokenPairDTO  = oAuthService.resolveTokenPair(request);

        // access token 로부터 user find
        User userByAccessToken = oAuthService.getUserByAccessToken(tokenPairDTO.getAccessToken());

        // refresh token 유효성 검사
        oAuthService.validateRefreshToken(userByAccessToken, tokenPairDTO.getRefreshToken());

        // access token, refresh token 새로 발급
        TokenPairDTO newTokenPairDTO = oAuthService.updateTokenPair(userByAccessToken);

        // response header에 새로 발급한 token pair set
        response.setHeader("Authorization-refresh", "Bearer " + newTokenPairDTO.getRefreshToken());
        response.setHeader("Authorization", "Bearer " + newTokenPairDTO.getAccessToken());

        System.out.println("tokenPairDTO.getAccessToken() = " + newTokenPairDTO.getAccessToken());
        System.out.println("tokenPairDTO.getRefreshToken() = " + newTokenPairDTO.getRefreshToken());
        
        // return
        return new BaseResponse<>("토큰 갱신 요청 성공");
    }
}
