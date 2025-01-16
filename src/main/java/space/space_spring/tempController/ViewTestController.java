package space.space_spring.tempController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ViewTestController {

    /**
     * 카카오 로그인 요청 처리
     * 카카오 인증 서버의 인증 및 동의 요청 페이지로 redirect
     */
    @Value("${oauth.kakao.client.id}")
    private String clientId;

    @Value("${oauth.kakao.redirect.uri}")
    private String redirectUri;

    @GetMapping("/oauth/kakao")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+clientId);
        url.append("&redirect_uri="+redirectUri);
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }
}
