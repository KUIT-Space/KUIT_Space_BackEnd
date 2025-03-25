package space.space_spring.domain.post.adapter.out.persistence.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.adapter.out.persistence.post.custom.PostRepositoryCustom;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;

public interface PostRepository extends JpaRepository<PostJpaEntity, Long>, PostRepositoryCustom {
    @Query("SELECT p FROM PostJpaEntity p " +
            "JOIN FETCH p.postBase pb " +
            "WHERE pb.board.id = :boardId " +
            "AND pb.status = :status")
    List<PostJpaEntity> findPostsByBoardId(@Param("boardId") Long boardId,
                                           @Param("status") BaseStatusType status);
    @Query("SELECT p FROM PostJpaEntity p " +
            "JOIN FETCH p.postBase pb " +
            "WHERE pb.discordId = :discordId " +
            "AND pb.status = :status")
    PostJpaEntity findByDiscordId(@Param("discordId") Long discordId,
                                      @Param("status") BaseStatusType status);

}
