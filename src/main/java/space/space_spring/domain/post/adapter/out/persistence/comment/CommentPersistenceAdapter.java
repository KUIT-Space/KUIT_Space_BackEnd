package space.space_spring.domain.post.adapter.out.persistence.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort {

    private final SpringDataPostCommentRepository postCommentRepository;

    @Override
    public Map<Long, Long> countCommentsByPostIds(List<Long> postIds) {
        List<PostCommentCount> results = postCommentRepository.countCommentsByPostIds(postIds);
        return results.stream()
                .collect(Collectors.toMap(PostCommentCount::getPostId, PostCommentCount::getCommentCount));
    }
}
