package space.space_spring.domain.authorization.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.authorization.auth.service.AuthService;
import space.space_spring.domain.user.model.PostLoginDto;
import space.space_spring.exception.CustomException;
import space.space_spring.response.BaseResponse;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_USER_LOGIN;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<PostLoginDto.Response> login(@Validated @RequestBody PostLoginDto.Request request, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_USER_LOGIN, getErrorMessage(bindingResult));
        }

        PostLoginDto login = authService.login(request);

        System.out.println("login.getTokenPairDTO().getRefreshToken() = " + login.getTokenPairDTO().getRefreshToken());
        System.out.println("login.getTokenPairDTO().getAccessToken() = " + login.getTokenPairDTO().getAccessToken());

        response.setHeader("Authorization-refresh", "Bearer " + login.getTokenPairDTO().getRefreshToken());
        response.setHeader("Authorization", "Bearer " + login.getTokenPairDTO().getAccessToken());

        return new BaseResponse<>(new PostLoginDto.Response(login.getUserId()));
    }
}
