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
import space.space_spring.exception.CustomException;
import space.space_spring.util.user.UserUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.CHATROOM_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserChatRoomService {

    private final UserUtils userUtils;

    private final ChatRoomDao chatRoomDao;
    private final UserChatRoomDao userChatRoomDao;

//    @Transactional
//    public void saveLastReadTime(Long userId, Long chatRoomId) {
//        User userByUserId = userUtils.findUserByUserId(userId);
//        ChatRoom chatRoomByChatRoomId = chatRoomDao.findById(chatRoomId)
//                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));
//
//        UserChatRoom targetChatRoom = userChatRoomDao.findByUserAndChatRoom(userByUserId, chatRoomByChatRoomId);
//        targetChatRoom.setLastReadTime(LocalDateTime.now());
////      userChatRoomDao.save(targetChatRoom);
//        log.info("socket disconnect 시 마지막으로 읽은 시간" + targetChatRoom.getLastReadTime());
//    }
}
