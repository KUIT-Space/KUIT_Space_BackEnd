package space.space_spring.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.dto.PostUserRequest;
import space.space_spring.dto.PostUserResponse;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.UserService;

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
    public BaseResponse<PostUserResponse> signup(@Validated @RequestBody PostUserRequest postUserRequest) {
        return new BaseResponse<>(userService.signup(postUserRequest));
    }

}
