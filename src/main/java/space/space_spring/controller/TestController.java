package space.space_spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.argument_resolver.jwtUserSpace.JwtUserSpaceAuth;
import space.space_spring.dto.chat.ChatTestRequest;
import space.space_spring.dto.chat.ChatTestResponse;
import space.space_spring.dto.jwt.JwtPayloadDto;
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

    @GetMapping("/test111")
    public BaseResponse<String> jwtUserSpaceTest(@JwtUserSpaceAuth JwtPayloadDto jwtPayloadDto) {
        log.info("jwtPayloadDto = {}", jwtPayloadDto);
        return new BaseResponse<>("jwt user space test 성공");
    }

    @MessageMapping("/chat/{spaceChatId}") // {spaceChatId} 채팅방으로 보낸 메세지 매핑
    @SendTo("/topic/chat/{spaceChatId}") // {spaceChatId} 채팅방을 구독한 곳들로 메세지 전송
    public ChatTestResponse sendMsgTest(@Payload ChatTestRequest chat, @DestinationVariable String spaceChatId) {
        log.info(spaceChatId + " 채팅방으로 " + chat.getMsg() + " 전송");
        return ChatTestResponse.of(chat.getMsg());
    }

    @SubscribeMapping("/topic/chat/{spaceChatId}") // {spaceChatId} 채팅방을 구독
    public void subscribeTest(@DestinationVariable String spaceChatId) {
        log.info(spaceChatId + " 채팅방 구독");
    }
}
