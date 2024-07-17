package space.space_spring.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.dto.user.PostUserLoginRequest;
import space.space_spring.dto.user.PostUserLoginResponse;
import space.space_spring.dto.user.PostUserSignupRequest;
import space.space_spring.dto.user.PostUserSignupResponse;
import space.space_spring.exception.UserException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.UserService;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.util.BindingResultUtils.getErrorMessage;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public BaseResponse<PostUserSignupResponse> signup(@Validated @RequestBody PostUserSignupRequest postUserSignupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_SIGNUP, getErrorMessage(bindingResult));
        }
        return new BaseResponse<>(userService.signup(postUserSignupRequest));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<PostUserLoginResponse> login(@Validated @RequestBody PostUserLoginRequest postUserLoginRequest, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_LOGIN, getErrorMessage(bindingResult));
        }

        String jwtLogin = userService.login(postUserLoginRequest);
        response.setHeader("Authorization", "Bearer " + jwtLogin);
        return new BaseResponse<>(new PostUserLoginResponse("로그인 성공"));
    }



}
