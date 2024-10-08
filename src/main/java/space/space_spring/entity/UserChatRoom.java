package space.space_spring.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("마지막으로 읽은 시간")
    @Nullable
    @Column(name = "last_read_time")
    private LocalDateTime lastReadTime;

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

    public LocalDateTime getEncodedTime() {
        return this.getLastReadTime()
                .atZone(ZoneId.of("UTC")) // UTC로 해석
                .withZoneSameInstant(ZoneId.of("Asia/Seoul")) // 서울 시간대로 변환
                .toLocalDateTime();
    }

    public void setUserRejoin() {
        this.setLastReadTime(LocalDateTime.now());
        this.updateActive();
    }
}
