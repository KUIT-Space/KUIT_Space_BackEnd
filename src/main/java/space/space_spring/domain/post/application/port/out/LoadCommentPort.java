package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Comment;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;
import java.util.Map;

public interface LoadCommentPort {
    
    Map<Long, NaturalNumber> countCommentsByPostIds(List<Long> postIds);

    Comment loadById(Long commentId);

    List<Comment> loadByPostIdWithoutStatusFilter(Long postId);     // status 값 상관없이 모든 댓글들을 조회
}
