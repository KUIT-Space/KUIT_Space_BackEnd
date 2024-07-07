package space.space_spring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "CI/CD 환경 구축 테스트 중. 이 메세지가 보인다면 성공입니다";
    }
}
