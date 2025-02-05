package space.space_spring.domain.user.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.user.application.port.in.OauthUseCase;
import space.space_spring.domain.user.application.port.in.SignInUseCase;
import space.space_spring.domain.user.application.port.in.SignUpUseCase;
import space.space_spring.domain.user.domain.User;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthUseCase oauthUseCase;
    private final SignInUseCase signInUseCase;
    private final SignUpUseCase signUpUseCase;

    @GetMapping("/oauth/discord")
    public BaseResponse<SuccessResponse> signInDiscord(@RequestParam String code) throws JsonProcessingException {
        String accessToken = oauthUseCase.getAccessToken(code);
        User user = oauthUseCase.getUserInfo(accessToken);
        boolean userExist = signInUseCase.userExist(user);
        if (userExist) {
            return new BaseResponse<>(new SuccessResponse(signInUseCase.signIn(user)));
        }
        return new BaseResponse<>(new SuccessResponse(signUpUseCase.signUp(user)));
    }

}
