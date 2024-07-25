package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.chat.ChatRoomDao;
import space.space_spring.dao.chat.UserChatRoomDao;
import space.space_spring.dto.chat.ChatRoomResponse;
import space.space_spring.dto.chat.ReadChatRoomResponse;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
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
                    Long id = cr.getId();
                    String name = cr.getName();
                    String imgUrl = cr.getImg();
                    // TODO: chatting message 처리
                    // String lastMsg = cr.get
                    String lastMsg = "메시지 관련 처리 예정";
                    String lastTime = "메시지 관련 처리 예정";
                    int unreadMsgCount = 1;
                    return ChatRoomResponse.of(id, name, imgUrl, lastMsg, lastTime, unreadMsgCount);
                })
                .toList()
        );
    }
}
