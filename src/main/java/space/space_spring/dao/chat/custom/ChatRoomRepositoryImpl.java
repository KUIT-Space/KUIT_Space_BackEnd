package space.space_spring.dao.chat.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

import java.util.List;
import space.space_spring.entity.enumStatus.BaseStatusType;

import static space.space_spring.entity.QChatRoom.chatRoom;
import static space.space_spring.entity.QUserChatRoom.userChatRoom;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRoom> findByUserAndSpace(User who, Space where) {
        return jpaQueryFactory
                .selectFrom(chatRoom)
                .join(userChatRoom).on(
                        userChatRoom.chatRoom.eq(chatRoom)
                        .and(userChatRoom.user.eq(who))
                        .and(userChatRoom.status.eq(BaseStatusType.ACTIVE)))
                .where(chatRoom.space.eq(where)
                        .and(chatRoom.status.eq(BaseStatusType.ACTIVE)))
                .orderBy(chatRoom.lastModifiedAt.desc())
                .fetch();
    }
}
