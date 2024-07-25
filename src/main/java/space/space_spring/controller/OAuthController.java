package space.space_spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import space.space_spring.dto.oAuthDto.KakaoLoginDto;
import space.space_spring.dto.oAuthInfo.KakaoInfo;
import space.space_spring.service.OAuthService;

@Controller
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
     * 카카오 로그인 요청 처리
     * 카카오 인증 서버의 인증 및 동의 요청 페이지로 redirect
     */
    @GetMapping("/kakao")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+clientId);
        url.append("&redirect_uri="+redirectUri);
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    /**
     * 유저가 카카오 로그인 동의 시 호출될 콜백 함수
     */
    @GetMapping("/callback/kakao")
    public String kakaoCallback(String code, HttpSession session) {

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
        // 유저 email 정보가 db에 없을 시 -> 새로 계정 생성
        // 유저 email 정보가 db에 있을 시 -> 같은 계정으로 판단
        oAuthService.findUserByOAuthInfo(kakaoInfo);

        return "redirect:/";
    }
}
