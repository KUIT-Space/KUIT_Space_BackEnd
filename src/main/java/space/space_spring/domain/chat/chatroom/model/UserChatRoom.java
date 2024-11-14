package space.space_spring.domain.chat.chatroom.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import space.space_spring.entity.BaseEntity;
import space.space_spring.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("유저별 채팅방")
@Table(name = "User_Chat_Room")
public class UserChatRoom extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_chat_id")
    private Long id;

    @Comment("채팅방 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Comment("사용자 ID")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("마지막으로 읽은 시간")
    @Nullable
    @Column(name = "last_read_time")
    private LocalDateTime lastReadTime;

    @Builder
    private UserChatRoom(Long id, ChatRoom chatRoom, User user, @Nullable LocalDateTime lastReadTime) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.user = user;
        this.lastReadTime = lastReadTime;
    }

    public static UserChatRoom of(ChatRoom chatRoom, User user, LocalDateTime lastReadTime) {
        return UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(user)
                .lastReadTime(lastReadTime)
                .build();
    }

    public void setLastReadTime(LocalDateTime lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public void setUserRejoin() {
        this.setLastReadTime(LocalDateTime.now());
        this.updateActive();
    }
}
