package space.space_spring.dao.chat.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

import java.util.List;

import static space.space_spring.entity.QChatRoom.chatRoom;
import static space.space_spring.entity.QUser.user;
import static space.space_spring.entity.QUserChatRoom.userChatRoom;

@RequiredArgsConstructor
public class ChatRoomDaoImpl implements ChatRoomDaoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRoom> findByUserAndSpace(User who, Space where) {
        return jpaQueryFactory
                .selectFrom(chatRoom)
                .join(chatRoom, userChatRoom.chatRoom)
                .where(user.eq(who)
                        .and(chatRoom.space.eq(where)))
                .orderBy(chatRoom.lastModifiedAt.desc())
                .fetch();
    }
}
