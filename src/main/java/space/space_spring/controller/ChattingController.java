package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import space.space_spring.dto.chat.request.ChatMessageRequest;
import space.space_spring.dto.chat.response.ChatMessageLogResponse;
import space.space_spring.dto.chat.response.ChatMessageResponse;
import space.space_spring.service.ChattingService;
import space.space_spring.service.UserChatRoomService;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    private final UserChatRoomService userChatRoomService;

    @MessageMapping("/chat/{chatRoomId}") // {chatRoomId} 채팅방으로 보낸 메세지 매핑
    @SendTo("/topic/chat/{chatRoomId}") // {chatRoomId} 채팅방을 구독한 곳들로 메세지 전송
    public ChatMessageResponse sendChatMessage (@Payload ChatMessageRequest chatMessageRequest, @DestinationVariable Long chatRoomId,
                                                @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) throws IOException {
        Long senderId = (Long) sessionAttributes.get("userId");
//        log.info(senderId + " 님이 " + chatRoomId + " 채팅방으로 " + chatMessageRequest.getContent() + " 전송");

        return chattingService.sendChatMessage(senderId, chatMessageRequest, chatRoomId);
    }

    @SubscribeMapping("/chat/{chatRoomId}") // {chatRoomId} 채팅방을 구독
    public ChatMessageLogResponse subscribeChatRoom (@DestinationVariable Long chatRoomId, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
//        log.info(chatRoomId + " 채팅방 구독");
        sessionAttributes.put("chatRoomId", chatRoomId);
        return chattingService.readChatMessageLog(chatRoomId);
    }

    // socket disconnect 시 호출
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        Long userId = (Long) sessionAttributes.get("userId");
        Long chatRoomId = (Long) sessionAttributes.get("chatRoomId");

        userChatRoomService.saveLastReadTime(userId, chatRoomId);
    }
}
