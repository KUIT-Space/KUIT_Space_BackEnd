package space.space_spring.domain.voiceRoom.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.voiceRoom.model.entity.VoiceRoom;

@Repository
public class VoiceRoomDao  {
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public Long createVoiceRoom(String name,int order,Space space){
        try {
            VoiceRoom voiceRoom = VoiceRoom.createVoiceRoom(name, order, space);
            entityManager.persist(voiceRoom);
            entityManager.flush();

            return voiceRoom.getVoiceRoomId();
        }catch (Exception e){
            System.out.print("create voiceRoom exception:"+e);
            //Todo 예외 처리 필요
            return null;
        }
    }


}


