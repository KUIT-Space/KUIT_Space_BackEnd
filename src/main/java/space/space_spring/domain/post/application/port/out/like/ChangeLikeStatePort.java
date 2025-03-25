package space.space_spring.domain.post.application.port.out.like;

public interface ChangeLikeStatePort {

    void changeLikeState(Long targetId, Long spaceMemberId, boolean changeTo);
}
