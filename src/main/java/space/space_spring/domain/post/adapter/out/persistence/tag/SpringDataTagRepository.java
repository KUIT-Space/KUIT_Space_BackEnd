package space.space_spring.domain.post.adapter.out.persistence.tag;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.adapter.out.persistence.tag.custom.TagRepositoryCustom;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface SpringDataTagRepository extends JpaRepository<TagJpaEntity, Long>, TagRepositoryCustom {
    Optional<TagJpaEntity> findByIdAndStatus(Long tagId, BaseStatusType status);

    Optional<TagJpaEntity> findByIdAndBoardIdAndStatus(Long tagId, Long boardId, BaseStatusType status);

    @Query("SELECT t FROM TagJpaEntity t WHERE t.board.id IN :boardIds")
    List<TagJpaEntity> findTagsByBoardIds(@Param("boardIds") List<Long> boardIds);


    @Query("SELECT t FROM TagJpaEntity t WHERE t.discordId IN :discordIdOfTag AND t.status = :status")
    List<TagJpaEntity> findAllByDiscordIdAndStatus(@Param("discordIdOfTag") List<Long> discordIdOfTag, @Param("status")BaseStatusType baseStatusType);

    @Query("SELECT t FROM TagJpaEntity t WHERE t.id IN :tagId AND t.status = :status")
    List<TagJpaEntity> findAllByIdAndStatus(@Param("tagId") List<Long> tagIds, @Param("status")BaseStatusType baseStatusType);

    Optional<TagJpaEntity> findByBoardId(Long boardId);
}
