package space.space_spring.domain.post.application.port.out.like;

import space.space_spring.domain.post.domain.Like;

public interface ChangeLikeStatePort {

    void changeLikeState(Long targetId, Long spaceMemberId);
}
