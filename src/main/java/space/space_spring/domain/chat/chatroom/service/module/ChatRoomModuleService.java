package space.space_spring.domain.chat.chatroom.service.module;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.CHATROOM_NOT_EXIST;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.domain.chat.chatroom.repository.ChatRoomRepository;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

@Component
@RequiredArgsConstructor
public class ChatRoomModuleService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom findChatRoom(Long chatRoomId) {
        return Optional.ofNullable(this.chatRoomRepository.findByIdAndStatus(chatRoomId, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));
    }

    @Transactional
    public List<ChatRoom> findChatRooms(Long chatRoomId, Long spaceId) {
        return this.chatRoomRepository.findByUserIdAndSpaceId(chatRoomId, spaceId);
    }

    @Transactional
    public ChatRoom save(ChatRoom chatRoom) {
        return this.chatRoomRepository.save(chatRoom);
    }
}
