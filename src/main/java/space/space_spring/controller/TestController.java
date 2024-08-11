package space.space_spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuth;

import space.space_spring.argumentResolver.userSpace.CheckUserSpace;
import space.space_spring.argumentResolver.userSpace.UserSpaceAuth;
import space.space_spring.argumentResolver.userSpace.UserSpaceId;
import space.space_spring.response.BaseResponse;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "CI/CD 환경 구축 테스트 중. 이 메세지가 보인다면 성공입니다";
    }

    @GetMapping("/test/jwt")
    public BaseResponse<String> jwtLoginTest(@JwtLoginAuth Long userId) {
        log.info("userId = {}", userId);
        return new BaseResponse<>("jwt login test 성공");
    }

    @GetMapping("/space/{spaceId}/test")
    public BaseResponse<String> LoginTest(
            @JwtLoginAuth Long userId,
            @UserSpaceId Long userSpaceId,
            @UserSpaceAuth String userSpaceAuth) {
        log.info("userId = {}", userId);
        return new BaseResponse<>("{ userId : "+ userId.toString()
                +""
                +"userSpaceId : "+userSpaceId
                +""
                +"userSpaceAuth : "+userSpaceAuth

                + "}"
        );
    }

    @GetMapping("/space/{spaceId}/test/pass")
    @CheckUserSpace(required = false)
    public  BaseResponse<String> LoginPassAnnotaionTest(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId
            ){
        return new BaseResponse<>("{ userId : "+ userId.toString()
                +"");
    }



}
