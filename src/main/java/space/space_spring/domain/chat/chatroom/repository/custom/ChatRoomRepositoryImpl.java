package space.space_spring.domain.chat.chatroom.repository.custom;

import static space.space_spring.domain.chat.chatroom.model.QChatRoom.chatRoom;
import static space.space_spring.domain.chat.chatroom.model.QUserChatRoom.userChatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;

import java.util.List;
import space.space_spring.global.common.enumStatus.BaseStatusType;


@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRoom> findByUserIdAndSpaceId(Long userId, Long spaceId) {
        return jpaQueryFactory
                .selectFrom(chatRoom)
                .join(userChatRoom).on(
                        userChatRoom.chatRoom.eq(chatRoom)
                        .and(userChatRoom.user.userId.eq(userId))
                        .and(userChatRoom.status.eq(BaseStatusType.ACTIVE)))
                .where(chatRoom.space.spaceId.eq(spaceId)
                        .and(chatRoom.status.eq(BaseStatusType.ACTIVE)))
                .orderBy(chatRoom.lastModifiedAt.desc())
                .fetch();
    }
}
