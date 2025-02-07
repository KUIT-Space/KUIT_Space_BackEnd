package space.space_spring.domain.user.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.user.application.port.in.OauthUseCase;
import space.space_spring.domain.user.domain.User;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthUseCase oauthUseCase;

    @GetMapping("/oauth/discord")
    public BaseResponse<SuccessResponse> signInDiscord(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = oauthUseCase.signInWithDiscord(code);
        response.setHeader("Authorization", "Bearer " + token);

        if (token == null || token.isEmpty()) {
            return new BaseResponse<>(new SuccessResponse(false));
        }
        return new BaseResponse<>(new SuccessResponse(true));
    }

}
