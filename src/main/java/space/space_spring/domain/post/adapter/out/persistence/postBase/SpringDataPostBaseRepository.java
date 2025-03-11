package space.space_spring.domain.post.adapter.out.persistence.postBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;
import java.util.Optional;

public interface SpringDataPostBaseRepository extends JpaRepository<PostBaseJpaEntity, Long> {
    Optional<PostBaseJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);

    @Query("SELECT pb FROM PostBaseJpaEntity pb " +
            "JOIN FETCH PostJpaEntity p ON p.postBase.id = pb.id " +
            "JOIN FETCH PostTagJpaEntity pt ON pt.postBase.id = pb.id " +
            "WHERE pt.tag.id = :tagId " +
            "AND pb.status = :status")
    List<PostBaseJpaEntity> findPostsByTagId(@Param("boardId") Long boardId,
                                             @Param("tagId") Long tagId,
                                             @Param("status") BaseStatusType status);
}
