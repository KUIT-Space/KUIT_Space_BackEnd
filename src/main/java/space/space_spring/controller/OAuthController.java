package space.space_spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.dto.oAuthDto.KakaoLoginDto;
import space.space_spring.dto.oAuthInfo.KakaoInfo;
import space.space_spring.entity.User;
import space.space_spring.response.BaseResponse;
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
}
