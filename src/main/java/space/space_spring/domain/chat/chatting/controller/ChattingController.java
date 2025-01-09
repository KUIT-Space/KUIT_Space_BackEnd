package space.space_spring.domain.chat.chatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argumentResolver.userSpace.CheckUserSpace;
import space.space_spring.domain.chat.chatting.model.request.ChatMessageRequest;
import space.space_spring.domain.chat.chatting.model.response.ChatMessageLogResponse;
import space.space_spring.domain.chat.chatting.model.response.ChatMessageResponse;
import space.space_spring.domain.chat.chatting.service.component.ChattingService;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;


    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/topic/chat/{chatRoomId}")
    @CheckUserSpace(required = false)
    public ChatMessageResponse sendChatMessage (@Payload ChatMessageRequest chatMessageRequest, @DestinationVariable Long chatRoomId,
                                                @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) throws IOException {
        Long senderId = (Long) sessionAttributes.get("userId");

        return chattingService.sendChatMessage(senderId, chatMessageRequest, chatRoomId);
    }

    @SubscribeMapping("/chat/{chatRoomId}")
    @CheckUserSpace(required = false)
    public ChatMessageLogResponse subscribeChatRoom (@DestinationVariable Long chatRoomId, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        sessionAttributes.put("chatRoomId", chatRoomId);
        return chattingService.readChatMessageLog(chatRoomId);
    }

}
