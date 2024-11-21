package space.space_spring.domain.chat.chatroom.service.module;

import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_CHATROOM;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.chat.chatroom.model.UserChatRoom;
import space.space_spring.domain.chat.chatroom.repository.UserChatRoomRepository;
import space.space_spring.entity.enumStatus.BaseStatusType;
import space.space_spring.exception.CustomException;

@Component
@RequiredArgsConstructor
public class UserChatRoomModuleService {

    private final UserChatRoomRepository userChatRoomRepository;

    @Transactional
    public UserChatRoom findUserChatRoom(Long userId, Long chatRoomId) {
        return Optional.ofNullable(this.userChatRoomRepository.findByUserIdAndChatRoomIdAndStatus(userId, chatRoomId,
                        BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_CHATROOM));
    }

    @Transactional
    public List<UserChatRoom> findUserChatRooms(Long chatRoomId) {
        return this.userChatRoomRepository.findByChatRoomIdAndStatus(chatRoomId, BaseStatusType.ACTIVE);
    }

    @Transactional
    public void save(UserChatRoom userChatRoom) {
        this.userChatRoomRepository.save(userChatRoom);
    }
}
