package space.space_spring.domain.post.adapter.out.persistence.tag;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.adapter.out.persistence.tag.custom.TagRepositoryCustom;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface SpringDataTagRepository extends JpaRepository<TagJpaEntity, Long>, TagRepositoryCustom {
    Optional<TagJpaEntity> findByIdAndStatus(Long tagId, BaseStatusType status);

    @Query("SELECT t FROM TagJpaEntity t WHERE t.board.id IN :boardIds")
    List<TagJpaEntity> findTagsByBoardIds(@Param("boardIds") List<Long> boardIds);
}
