package space.space_spring.domain.post.adapter.out.persistence.post.custom;

import java.util.List;
import java.util.Optional;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;

public interface PostRepositoryCustom {
    List<PostJpaEntity> findLatestByBoardIds(List<Long> boardIds, int size);

    Optional<PostJpaEntity> findLatestByBoardIdAndTagId(Long boardId, Long tagId);
}
