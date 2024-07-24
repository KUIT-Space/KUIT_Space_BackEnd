package space.space_spring.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Comment("채팅방")
@Table(name = "Chat_Room")
public class ChatRoom extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;

    @Comment("채팅방이 속한 스페이스 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Comment("채팅방 이름")
    @Column(name = "chat_room_name")
    private String name;

    @Comment("채팅방 이미지")
    @Nullable
    @Column(name = "chat_room_img")
    private String img;

    @Comment("마지막으로 전송된 메시지 ID")
    @Nullable
    @Column(name = "last_msg_id")
    private int lastMsgId;

    // 양방향 매핑
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<UserChatRoom> userChatRooms;
}
