package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Space;
import space.space_spring.entity.VoiceRoom;

import java.util.List;
@Repository
public interface VoiceRoomRepository extends JpaRepository<VoiceRoom,Long> {

    //Todo limit 개수 만큼 가져오도록 기능과 파라미터 수정
    List<VoiceRoom> findBySpace(Space space);
    @Query("SELECT MAX(r.order) FROM VoiceRoom r WHERE r.space = :space")
    Integer findMaxOrderBySpace(@Param("space") Space space);

    boolean existsByVoiceRoomId(long voiceRoomId);
    VoiceRoom findById(long Id);
}
