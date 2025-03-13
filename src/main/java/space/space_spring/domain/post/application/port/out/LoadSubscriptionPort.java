package space.space_spring.domain.post.application.port.out;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import space.space_spring.domain.post.domain.Subscription;

public interface LoadSubscriptionPort {

    Optional<Subscription> loadByInfos(Long spaceMemberId, Long boardId, Long tagId);

    Optional<Subscription> loadByBoardId(Long boardId);

    // 사용자가 구독한 게시판+태그 목록 조회
    List<Map.Entry<Long, Long>> loadSubscribedBoardTagPairs(Long spaceMemberId);
}
