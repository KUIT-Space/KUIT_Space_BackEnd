package space.space_spring.domain.chat.chatroom.service.module;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_CHATROOM;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.domain.chat.chatroom.model.UserChatRoom;
import space.space_spring.domain.chat.chatroom.repository.UserChatRoomRepository;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

@Component
@RequiredArgsConstructor
public class UserChatRoomModuleService {

    private final UserChatRoomRepository userChatRoomRepository;

    @Transactional
    public UserChatRoom findUserChatRoom(User user, ChatRoom chatRoom) {
        return Optional.ofNullable(
                        this.userChatRoomRepository.findByUserAndChatRoomAndStatus(user, chatRoom, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_CHATROOM));
    }

    @Transactional
    public List<UserChatRoom> findUserChatRooms(ChatRoom chatRoom) {
        return this.userChatRoomRepository.findByChatRoomAndStatus(chatRoom, BaseStatusType.ACTIVE);
    }

    @Transactional
    public void save(UserChatRoom userChatRoom) {
        this.userChatRoomRepository.save(userChatRoom);
    }
}
