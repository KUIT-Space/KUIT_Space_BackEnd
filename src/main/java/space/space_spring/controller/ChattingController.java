package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.dto.chat.request.ChatMessageRequest;
import space.space_spring.dto.chat.response.ChatMessageLogResponse;
import space.space_spring.dto.chat.response.ChatMessageResponse;
import space.space_spring.service.ChattingService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;
    @MessageMapping("/chat/{chatRoomId}") // {chatRoomId} 채팅방으로 보낸 메세지 매핑
    @SendTo("/topic/chat/{chatRoomId}") // {chatRoomId} 채팅방을 구독한 곳들로 메세지 전송
    public ChatMessageResponse sendChatMessage (@Payload ChatMessageRequest chatMessageRequest, @DestinationVariable Long chatRoomId,
                                                @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        Long senderId = (Long) sessionAttributes.get("userId");
        log.info(senderId + " 님이 " + chatRoomId + " 채팅방으로 " + chatMessageRequest.getContent() + " 전송");

        return chattingService.sendChatMessage(senderId, chatMessageRequest, chatRoomId);
    }

    @SubscribeMapping("/chat/{chatRoomId}") // {chatRoomId} 채팅방을 구독
    public ChatMessageLogResponse subscribeChatRoom (@DestinationVariable Long chatRoomId) {
        log.info(chatRoomId + " 채팅방 구독");
        return chattingService.readChatMessageLog(chatRoomId);
    }
}
