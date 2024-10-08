package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Space;
import space.space_spring.entity.VoiceRoom;

import java.util.List;
import java.util.Optional;

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


