package space.space_spring.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Comment("유저별 채팅방")
@Table(name = "User_Chat_Room")
public class UserChatRoom extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "user_chat_id")
    private Long id;

    @Comment("채팅방 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Comment("사용자 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("마지막으로 읽은 메시지 ID")
    @Nullable
    @Column(name = "last_read_msg_id")
    private Long lastReadMsgId;

    public UserChatRoom of(ChatRoom chatRoom, User user, Long lastReadMsgId) {
        return UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(user)
                .lastReadMsgId(lastReadMsgId)
                .build();
    }
}
