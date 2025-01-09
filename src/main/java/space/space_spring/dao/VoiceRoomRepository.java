package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.entity.VoiceRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoiceRoomRepository extends JpaRepository<VoiceRoom,Long> {


    @Query("SELECT v FROM VoiceRoom v WHERE v.space = :space AND v.status = 'ACTIVE'")
    List<VoiceRoom> findBySpace(@Param("space")Space space);
    @Query("SELECT MAX(r.order) FROM VoiceRoom r WHERE r.space = :space AND r.status = 'ACTIVE'")
    Integer findMaxOrderBySpace(@Param("space") Space space);

    @Query("SELECT MAX(r.order) FROM VoiceRoom r WHERE r.space.id = :spaceId AND r.status = 'ACTIVE'")
    Integer findMaxOrderBySpaceId(@Param("spaceId") Long spaceId);
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM VoiceRoom v WHERE v.voiceRoomId = :id AND v.status = 'ACTIVE'")
    boolean existsByVoiceRoomId(@Param("id")long voiceRoomId);
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM VoiceRoom v WHERE v.name = :voiceRoomName AND v.status = 'ACTIVE'")
    boolean existsByName(@Param("voiceRoomName") String voiceRoomName);
    @Query("SELECT v FROM VoiceRoom v WHERE v.voiceRoomId = :id AND v.status = 'ACTIVE'")
    Optional<VoiceRoom> findById(@Param("id") long Id);

    @Query("SELECT v FROM VoiceRoom v WHERE v.space.id = :spaceId AND v.status = 'ACTIVE'")
    List<VoiceRoom> findActiveVoiceRoomsBySpaceId(@Param("spaceId") Long spaceId);
}
