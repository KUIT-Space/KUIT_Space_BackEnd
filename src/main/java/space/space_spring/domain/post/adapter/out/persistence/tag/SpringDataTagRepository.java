package space.space_spring.domain.post.adapter.out.persistence.tag;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.custom.TagRepositoryCustom;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface SpringDataTagRepository extends JpaRepository<TagJpaEntity, Long>, TagRepositoryCustom {
    Optional<TagJpaEntity> findByIdAndStatus(Long tagId, BaseStatusType status);

    Optional<TagJpaEntity> findByIdAndBoardIdAndStatus(Long tagId, Long boardId, BaseStatusType status);
}
