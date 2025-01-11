package space.space_spring.domain.voiceRoom.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.voiceRoom.model.dto.RoomDto;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@Table(name = "VoiceRoom")
@Getter
@NoArgsConstructor
public class VoiceRoom extends BaseEntity {
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
    public static VoiceRoom createVoiceRoom(String name, int order, Space space){
        VoiceRoom voiceRoom= VoiceRoom.builder()
                .name(name)
                .order(order)
                .space(space)
                .build();
        voiceRoom.initializeBaseEntityFields();
        return voiceRoom;
    }
    public void update(String name,Integer order){
        if(name!=null){
            this.name = name;
        }
        if(order!=null){
            this.order=order;
        }
        this.onUpdate();
    }

    public RoomDto convertRoomDto(){
        return RoomDto.builder()
                .name(this.getName())
                //.createdAt(voiceRoom.)
                .id(this.getVoiceRoomId())
                .order(this.getOrder())
                .sid(null)
                .metadata(null)
                //.startTime()
                .participantListDto(null)
                .build();
    }
}
