package space.space_spring.domain.post.adapter.out.persistence.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_BASE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements CreatePostPort {

    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataPostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public Long createPost (Post post) {
        PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findById(post.getPostBaseId())
                .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

        PostJpaEntity postJpaEntity = postMapper.toJpaEntity(postBaseJpaEntity, post);

        return postRepository.save(postJpaEntity).getId();
    }

}
