package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.chat.ChatRoomDao;
import space.space_spring.dao.chat.ChattingDao;
import space.space_spring.dao.chat.UserChatRoomDao;
import space.space_spring.dto.chat.response.ChatRoomResponse;
import space.space_spring.dto.chat.request.CreateChatRoomRequest;
import space.space_spring.dto.chat.response.CreateChatRoomResponse;
import space.space_spring.dto.chat.response.ReadChatRoomResponse;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserChatRoom;
import space.space_spring.entity.document.ChatMessage;
import space.space_spring.entity.enumStatus.ChatMessageType;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserDao userDao;
    private final UserUtils userUtils;
    private final SpaceUtils spaceUtils;
    private final ChattingDao chattingDao;
    private final ChatRoomDao chatRoomDao;
    private final UserChatRoomDao userChatRoomDao;

    @Transactional
    public ReadChatRoomResponse readChatRooms(Long userId, Long spaceId) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: 해당 user의 해당 space 내의 채팅방 리스트 return
        List<ChatRoom> result = chatRoomDao.findByUserAndSpace(userByUserId, spaceBySpaceId);

        return ReadChatRoomResponse.of(result.stream()
                .map(cr -> {
                    // TODO 4: 각 채팅방의 마지막으로 업데이트된 메시지 정보 find
                    ChatMessage lastMsg = chattingDao.findTopByChatRoomIdOrderByCreatedAtDesc(cr.getId());
                    LocalDateTime lastUpdateTime = lastMsg.getCreatedAt();
                    String lastContent = switch (lastMsg.getMessageType()) {
                        case TEXT -> lastMsg.getContent().get("text");
                        /**
                         * TODO: 메시지 타입 관련하여 미리보기 뷰에 따라 변경 가능
                         */
//                        case IMG -> "img";
//                        case FILE -> "file";
//                        case POST -> "post";
//                        case PAY -> "pay";
                        default -> lastMsg.getMessageType().toString();
                    };

                    // TODO 5: 각 채팅방의 안읽은 메시지 개수 계산
                    UserChatRoom userChatRoom = userChatRoomDao.findByUserAndChatRoom(userByUserId, cr);
                    LocalDateTime lastReadTime = userChatRoom.getLastReadTime();
                    int unreadMsgCount = chattingDao.countByChatRoomIdAndCreatedAtBetween(
                            cr.getId(),
                            lastReadTime,
                            lastUpdateTime
                    );

                    return ChatRoomResponse.of(cr, lastContent, String.valueOf(lastUpdateTime), unreadMsgCount);
                })
                .toList()
        );
    }

    @Transactional
    public CreateChatRoomResponse createChatRoom(Long userId, Long spaceId, CreateChatRoomRequest createChatRoomRequest, String chatRoomImgUrl) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: chatRoom 생성 및 저장
        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.of(spaceBySpaceId, createChatRoomRequest, chatRoomImgUrl));

        // TODO 4: user_chatRoom 매핑 정보 저장
        UserChatRoom userChatRoom = userChatRoomDao.save(UserChatRoom.of(chatRoom, userByUserId, LocalDateTime.now()));

        // TODO 5: chatroom id 반환
        return CreateChatRoomResponse.of(chatRoom.getId());
    }
}
