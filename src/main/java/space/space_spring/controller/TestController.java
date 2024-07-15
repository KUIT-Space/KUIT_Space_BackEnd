package space.space_spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argument_resolver.JwtPreAuth;
import space.space_spring.response.BaseResponse;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "CI/CD 환경 구축 테스트 중. 이 메세지가 보인다면 성공입니다";
    }

    @GetMapping("/test/jwt")
    public BaseResponse<String> jwtTest(@JwtPreAuth Long userId) {
        log.info("userId = {}", userId);
        return new BaseResponse<>("jwt test 성공");
    }
}
