package space.space_spring.domain.post.adapter.out.persistence.postTag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.SpringDataTagRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;
import space.space_spring.domain.post.application.port.out.CreatePostTagPort;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_BASE_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostTagPersistenceAdapter implements CreatePostTagPort {

    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataTagRepository tagRepository;
    private final SpringDataPostTagRepository postTagRepository;

    @Override
    public void createPostTag(Long postId, Long tagId) {
        // 1. Tag 조회
        TagJpaEntity tagJpaEntity = tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException(TAG_NOT_FOUND));

        // 2. PostBase 조회
        PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

        // 3. PostTag 저장
        PostTagJpaEntity postTagJpaEntity = PostTagJpaEntity.create(postBaseJpaEntity, tagJpaEntity);
        postTagRepository.save(postTagJpaEntity);
    }
}
