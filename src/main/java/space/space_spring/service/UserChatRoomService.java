package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.chat.ChatRoomDao;
import space.space_spring.dao.chat.UserChatRoomDao;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.User;
import space.space_spring.entity.UserChatRoom;
import space.space_spring.util.user.UserUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserChatRoomService {

    private final UserUtils userUtils;

    private final ChatRoomDao chatRoomDao;
    private final UserChatRoomDao userChatRoomDao;

    @Transactional
    public void saveLastReadTime(Long userId, Long chatRoomId) {
        User userByUserId = userUtils.findUserByUserId(userId);
        Optional<ChatRoom> chatRoomByChatRoomId = chatRoomDao.findById(chatRoomId);

        chatRoomByChatRoomId.ifPresent(chatRoom -> {
            UserChatRoom targetChatRoom = userChatRoomDao.findByUserAndChatRoom(userByUserId, chatRoom);
            targetChatRoom.setLastReadTime(LocalDateTime.now());
        });
    }
}
