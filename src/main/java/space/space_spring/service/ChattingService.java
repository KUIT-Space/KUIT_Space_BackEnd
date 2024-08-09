package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.chat.ChattingDao;
import space.space_spring.dto.chat.request.ChatMessageRequest;
import space.space_spring.dto.chat.response.ChatMessageLogResponse;
import space.space_spring.dto.chat.response.ChatMessageResponse;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.entity.document.ChatMessage;
import space.space_spring.exception.UserSpaceException;
import space.space_spring.util.user.UserUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final UserUtils userUtils;
    private final SpaceDao spaceDao;
    private final UserSpaceDao userSpaceDao;
    private final ChattingDao chattingDao;

    @Transactional
    public ChatMessageResponse sendChatMessage(Long senderId, ChatMessageRequest chatMessageRequest, Long chatRoomId) {

        // TODO 1: 메시지 메타데이터 저장 위해 전송자 찾기
        User sender = userUtils.findUserByUserId(senderId);
        Space space = spaceDao.findSpaceBySpaceId(chatMessageRequest.getSpaceId());
        Optional<UserSpace> senderInSpace = Optional.ofNullable(userSpaceDao.findUserSpaceByUserAndSpace(sender, space)
                .orElseThrow(() -> new UserSpaceException(USER_IS_NOT_IN_SPACE)));

        // TODO 2: validation 후 전송자 프로필 사진 get
        String senderProfileImg = "";
        if (senderInSpace.isPresent()) {
            senderProfileImg = senderInSpace.get().getUserProfileImg();
        }

        // TODO 3: DB에 메시지 저장
        ChatMessage message = chattingDao.insert(ChatMessage.of(
                chatMessageRequest.getContent(),
                chatRoomId,
                chatMessageRequest.getSpaceId(),
                senderId,
                sender.getUserName(),
                senderProfileImg,
                chatMessageRequest.getMessageType(),
                LocalDateTime.now(ZoneId.of("Asia/Seoul"))
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
