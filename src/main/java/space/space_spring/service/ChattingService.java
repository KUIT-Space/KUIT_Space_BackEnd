package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.chat.ChattingDao;
import space.space_spring.dto.chat.request.ChatMessageRequest;
import space.space_spring.dto.chat.response.ChatMessageLogResponse;
import space.space_spring.dto.chat.response.ChatMessageResponse;
import space.space_spring.entity.UserSpace;
import space.space_spring.entity.document.ChatMessage;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChattingService {

    private final UserSpaceUtils userSpaceUtils;
    private final ChattingDao chattingDao;

    @Transactional
    public ChatMessageResponse sendChatMessage(Long senderId, ChatMessageRequest chatMessageRequest, Long chatRoomId) {

        // TODO 1: 메시지 메타데이터 저장 위해 전송자 찾기
        Optional<UserSpace> senderInSpace = userSpaceUtils.isUserInSpace(senderId, chatMessageRequest.getSpaceId());

        // TODO 2: validation 후 전송자 이름 및 프로필 사진 get
        String senderName = "";
        String senderProfileImg = "";
        if (senderInSpace.isPresent()) {
            senderName = senderInSpace.get().getUserName();
            senderProfileImg = senderInSpace.get().getUserProfileImg();
        }

        // TODO 3: DB에 메시지 저장
        ChatMessage message = chattingDao.insert(ChatMessage.of(
                chatMessageRequest,
                chatRoomId,
                senderId,
                senderName,
                senderProfileImg
        ));

        return ChatMessageResponse.of(message);
    }

    public ChatMessageLogResponse readChatMessageLog(Long chatRoomId) {
        List<ChatMessage> chatMessageList = chattingDao.findByChatRoomId(chatRoomId);
        return ChatMessageLogResponse.of(chatMessageList.stream()
                .map(ChatMessageResponse::of)
                .collect(Collectors.toList()));
    }
}
