package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.chat.ChatRoomDao;
import space.space_spring.dao.chat.UserChatRoomDao;
import space.space_spring.dto.chat.ChatRoomResponse;
import space.space_spring.dto.chat.CreateChatRoomRequest;
import space.space_spring.dto.chat.CreateChatRoomResponse;
import space.space_spring.dto.chat.ReadChatRoomResponse;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserChatRoom;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserDao userDao;
    private final UserUtils userUtils;
    private final SpaceUtils spaceUtils;
    private final ChatRoomDao chatRoomDao;
    private final UserChatRoomDao userChatRoomDao;

    public ReadChatRoomResponse readChatRooms(Long userId, Long spaceId) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: 해당 user의 해당 space 내의 채팅방 리스트 select
        List<ChatRoom> result = chatRoomDao.findByUserAndSpace(userByUserId, spaceBySpaceId);
        log.info(result.toString());

        return ReadChatRoomResponse.of(result.stream()
                .map(cr -> {
                    // TODO: chatting message 처리
                    String lastMsg = "메시지 관련 처리 예정";
                    String lastTime = "메시지 관련 처리 예정";
                    int unreadMsgCount = 1;
                    return ChatRoomResponse.of(cr, lastMsg, lastTime, unreadMsgCount);
                })
                .toList()
        );
    }

    @Transactional
    public CreateChatRoomResponse createChatRoom(Long userId, Long spaceId, CreateChatRoomRequest createChatRoomRequest) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: chatRoom 생성 및 저장
        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.of(spaceBySpaceId, createChatRoomRequest));
        log.info(chatRoom.toString());

        // TODO 4: user_chatRoom 매핑 정보 저장
        // TODO: 메시지 관련 처리 예정
        UserChatRoom userChatRoom = userChatRoomDao.save(UserChatRoom.of(chatRoom, userByUserId, null));
        log.info(userChatRoom.toString());

        // TODO 5: chatroom id 반환
        return CreateChatRoomResponse.of(chatRoom.getId());
    }
}
