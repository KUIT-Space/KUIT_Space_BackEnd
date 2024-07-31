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
    public boolean createVoiceRoom(String name,int order,Space space){
        try {
            entityManager.persist(VoiceRoom.createVoiceRoom(name, order, space));
            return true;
        }catch (Exception e){
            System.out.print("create voiceRoom exception:"+e);
            return false;
        }
    }
    @Transactional
    public Integer findMaxOrderBySpace(Space space) {
        String jpql = "SELECT MAX(r.order) FROM voice_room r WHERE r.space = :space";

        return entityManager.createQuery(jpql, Integer.class)
                .setParameter("space", space)
                .getSingleResult();
    }

    @Transactional
    public VoiceRoom findRoomWithMaxOrderBySpace(Space space) {
        String jpql = "SELECT r FROM VoiceRoom r WHERE r.space = :space AND r.order = (SELECT MAX(r2.order) FROM VoiceRoom r2 WHERE r2.space = :space)";

        return entityManager.createQuery(jpql, VoiceRoom.class)
                .setParameter("space", space)
                .getSingleResult();
    }


}


