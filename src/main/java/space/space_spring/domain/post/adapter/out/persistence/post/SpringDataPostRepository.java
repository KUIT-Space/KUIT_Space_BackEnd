package space.space_spring.domain.post.adapter.out.persistence.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.adapter.out.persistence.post.custom.PostRepositoryCustom;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;
import java.util.Optional;

public interface SpringDataPostRepository extends JpaRepository<PostJpaEntity, Long>, PostRepositoryCustom {
    @Query("SELECT p FROM PostJpaEntity p " +
            "JOIN FETCH p.postBase pb " +
            "WHERE pb.board.id = :boardId " +
            "AND pb.status = :status")
    List<PostJpaEntity> findPostsByBoardId(@Param("boardId") Long boardId,
                                           @Param("status") BaseStatusType status);

    @Query(value = "SELECT p FROM PostJpaEntity p " +
            "JOIN FETCH p.postBase pb " +
            "WHERE pb.board.id = :boardId " +
            "AND pb.status = :status",
            countQuery = "SELECT COUNT(p) FROM PostJpaEntity p JOIN p.postBase pb WHERE pb.board.id = :boardId AND pb.status = :status")
    Page<PostJpaEntity> findPostsByBoardIdPaged(@Param("boardId") Long boardId,
                                               @Param("status") BaseStatusType status,
                                               Pageable pageable);

    Page<PostJpaEntity> findByPostBaseIdIn(List<Long> postBaseIds, Pageable pageable);


    @Query("SELECT p FROM PostJpaEntity p " +
            "JOIN FETCH p.postBase pb " +
            "WHERE pb.discordId = :discordId " +
            "AND pb.status = :status")
    PostJpaEntity findByDiscordId(@Param("discordId") Long discordId,
                                      @Param("status") BaseStatusType status);
}
