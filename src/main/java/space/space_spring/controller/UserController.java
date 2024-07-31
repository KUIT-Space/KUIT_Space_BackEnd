package space.space_spring.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.user.*;
import space.space_spring.exception.UserException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.UserService;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.List;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserSpaceUtils userSpaceUtils;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public BaseResponse<String> signup(@Validated @RequestBody PostUserSignupRequest postUserSignupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_SIGNUP, getErrorMessage(bindingResult));
        }

        Long signup = userService.signup(postUserSignupRequest);

        return new BaseResponse<>("로컬 회원가입 성공");
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

    /**
     * 유저가 속한 스페이스 리스트
     * (유저별 스페이스 선택 뷰)
     */
    @GetMapping("/space-choice")
    public BaseResponse<GetSpaceInfoForUserResponse> showUserSpaceList(@JwtLoginAuth Long userId,
                                                                       @RequestParam int size,
                                                                       @RequestParam Long lastUserSpaceId) {

        log.info("userId = {}", userId);

        return new BaseResponse<>(userService.getSpaceListForUser(userId, size, lastUserSpaceId));
    }

}
