package space.space_spring.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import space.space_spring.dto.chat.request.CreateChatRoomRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static ChatRoom of(Space space, CreateChatRoomRequest createChatRoomRequest, String chatRoomImgUrl) {
        return ChatRoom.builder()
                .space(space)
                .name(createChatRoomRequest.getName())
                .img(chatRoomImgUrl)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }

    //    // 양방향 매핑
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
//    private List<UserChatRoom> userChatRooms;
}
