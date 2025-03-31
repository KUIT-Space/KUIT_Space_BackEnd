package space.space_spring.domain.post.application.port.out.like;

import space.space_spring.domain.post.domain.Like;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;
import java.util.Map;

public interface LoadLikePort {

    Map<Long, NaturalNumber> countLikesByPostIds(List<Long> postIds);

    NaturalNumber countLikeByPostId(Long postId);

    boolean hasSpaceMemberLiked(Long spaceMemberId, Long targetId);

    List<Like> loadAllLikes(Long postId);
}
