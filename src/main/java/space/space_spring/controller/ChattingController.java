package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.dto.chat.request.ChatMessageRequest;
import space.space_spring.dto.chat.response.ChatMessageResponse;
import space.space_spring.service.ChattingService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;
    @MessageMapping("/chat/{chatRoomId}") // {chatRoomId} 채팅방으로 보낸 메세지 매핑
    @SendTo("/topic/chat/{chatRoomId}") // {chatRoomId} 채팅방을 구독한 곳들로 메세지 전송
    public ChatMessageResponse sendChatMessage (@Payload ChatMessageRequest chatMessageRequest, @DestinationVariable Long chatRoomId) {
        log.info(chatRoomId + " 채팅방으로 " + chatMessageRequest.getContent() + " 전송");
        return chattingService.sendChatMessage(chatMessageRequest, chatRoomId);
    }

    @SubscribeMapping("/topic/chat/{chatRoomId}") // {chatRoomId} 채팅방을 구독
    public void subscribeChatRoom (@DestinationVariable String chatRoomId) {
        log.info(chatRoomId + " 채팅방 구독");
    }
}
