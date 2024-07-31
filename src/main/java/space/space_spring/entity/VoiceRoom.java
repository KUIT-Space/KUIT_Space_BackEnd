package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.dto.VoiceRoom.GetVoiceRoomList;

@Entity
@Table(name = "VoiceRoom")
@Getter
@NoArgsConstructor
public class VoiceRoom extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name="voice_room_id")
    private long voiceRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="space_id")
    private Space space;

    @Column(name="name")
    private String name;

    @Column(name="order_num")
    private int order;


    @Builder
    public VoiceRoom(String name,int order,Space space){
        this.name = name;
        this.order = order;
        this.space = space;
    }
    public static VoiceRoom createVoiceRoom(String name,int order,Space space){
        VoiceRoom voiceRoom= VoiceRoom.builder()
                .name(name)
                .order(order)
                .space(space)
                .build();
        voiceRoom.initializeBaseEntityFields();
        return voiceRoom;
    }
}
