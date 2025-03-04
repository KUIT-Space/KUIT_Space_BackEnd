package space.space_spring.domain.post.adapter.out.persistence.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.LoadLikePort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LikePersistenceAdapter implements LoadLikePort {

    private final SpringDataLikeRepository likeRepository;

    @Override
    public Map<Long, Long> countLikesByPostIds(List<Long> postIds) {
        List<PostLikeCount> results = likeRepository.countLikesByPostIds(postIds);
        return results.stream().collect(Collectors.toMap(PostLikeCount::getPostId, PostLikeCount::getLikeCount));
    }

}
