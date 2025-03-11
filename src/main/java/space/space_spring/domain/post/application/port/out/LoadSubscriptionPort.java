package space.space_spring.domain.post.application.port.out;

import java.util.List;
import java.util.Optional;
import space.space_spring.domain.post.domain.Subscription;

public interface LoadSubscriptionPort {

    Optional<Subscription> loadByInfos(Long spaceMemberId, Long boardId, Long tagId);

    // 사용자가 구독한 게시판 ID 목록 조회
    List<Long> loadSubscribedBoardIds(Long spaceMemberId);
}
