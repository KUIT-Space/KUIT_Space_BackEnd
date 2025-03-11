package space.space_spring.domain.post.adapter.out.persistence.subscription;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.adapter.out.persistence.subscription.custom.SubscriptionRepositoryCustom;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionJpaEntity, Long>, SubscriptionRepositoryCustom {
    Optional<SubscriptionJpaEntity> findBySpaceMemberAndBoardIdAndTagId(SpaceMemberJpaEntity spaceMember, Long boardId, Long tagId);

    // 특정 사용자가 구독한 게시판 ID 조회
    @Query("SELECT s.board.id FROM SubscriptionJpaEntity s WHERE s.spaceMember.id = :spaceMemberId")
    List<Long> findSubscribedBoardIdsBySpaceMemberId(@Param("spaceMemberId") Long spaceMemberId);
}
