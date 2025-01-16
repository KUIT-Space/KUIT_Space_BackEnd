package space.space_spring.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.domain.user.model.GetUserProfileListDto;
import space.space_spring.domain.user.model.request.PostUserSignupRequest;
import space.space_spring.domain.user.model.response.GetSpaceInfoForUserResponse;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.domain.user.service.UserService;
import space.space_spring.global.util.userSpace.UserSpaceUtils;

import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

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
            throw new CustomException(INVALID_USER_SIGNUP, getErrorMessage(bindingResult));
        }

        userService.signup(postUserSignupRequest);

        return new BaseResponse<>("로컬 회원가입 성공");
    }


    /**
     * 유저가 속한 스페이스 리스트
     * (유저별 스페이스 선택 뷰)
     */
    @GetMapping("/space-choice")
    public BaseResponse<GetSpaceInfoForUserResponse> showUserSpaceList(@JwtLoginAuth Long userId,
                                                                       @RequestParam int size,
                                                                       @RequestParam Long lastUserSpaceId) {

        return new BaseResponse<>(userService.getSpaceListForUser(userId, size, lastUserSpaceId));
    }

    /**
     * 스페이스 전체 설정 -> 스페이스 프로필 관리 view
     */
    @GetMapping("/profile")
    public BaseResponse<GetUserProfileListDto.Response> showUserProfileList(@JwtLoginAuth Long userId) {

        return new BaseResponse<>(userService.getUserProfileList(userId));
    }

}
