package space.space_spring.domain.post.adapter.out.persistence.postTag;

import com.amazonaws.services.ec2.model.SearchTransitGatewayMulticastGroupsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.SpringDataTagRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;
import space.space_spring.domain.post.application.port.out.CreatePostTagPort;
import space.space_spring.domain.post.application.port.out.UpdatePostTagPort;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

import java.util.*;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_BASE_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostTagPersistenceAdapter implements CreatePostTagPort, UpdatePostTagPort {

    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataTagRepository tagRepository;
    private final SpringDataPostTagRepository postTagRepository;

    @Override
    public void createPostTag(Long postId, List<Long> tagIds) {
        // 1. Tag 조회
        List<TagJpaEntity> tagJpaEntities = tagRepository.findAllById(tagIds);

        // 2. PostBase 조회
        PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

        // 3. PostTag 저장
        List<PostTagJpaEntity> postTagJpaEntities = tagJpaEntities.stream()
                .map(tag -> PostTagJpaEntity.create(postBaseJpaEntity, tag))
                .toList();
        postTagRepository.saveAll(postTagJpaEntities);
    }

    @Override
    public void updatePostTag(Long postId, List<Long> tagIds) {
        List<PostTagJpaEntity> allByPostBaseIdAndStatus = postTagRepository.findAllByPostBaseIdAndStatus(postId, BaseStatusType.ACTIVE);

        Set<Long> newTagIdsForPost = new HashSet<>(tagIds);
        Set<PostTagJpaEntity> previousJpaEntities = new HashSet<>(allByPostBaseIdAndStatus);

        Iterator<PostTagJpaEntity> iterator = previousJpaEntities.iterator();
        while (iterator.hasNext()) {
            PostTagJpaEntity postTag = iterator.next();
            Long tagId = postTag.getTag().getId();          // 기존 게시글의 tag id
            if (newTagIdsForPost.contains(tagId)) {         // 수정한 게시글의 tag list에 해당 tag가 포함된다면
                newTagIdsForPost.remove(tagId);
                iterator.remove();
            }
        }

        for (PostTagJpaEntity postTag : previousJpaEntities) {      // set에 남아있는 jpa entity 들 -> inactive 처리
            postTag.updateToInactive();
        }

        List<PostTagJpaEntity> newJpaEntities = new ArrayList<>();
        for (Long tagId : newTagIdsForPost) {       // set에 남아있는 tag들 -> postTag 생성
            PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findByIdAndStatus(postId, BaseStatusType.ACTIVE)
                    .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));
            TagJpaEntity tagJpaEntity = tagRepository.findByIdAndStatus(tagId, BaseStatusType.ACTIVE)
                    .orElseThrow(() -> new CustomException(TAG_NOT_FOUND));

            newJpaEntities.add(PostTagJpaEntity.create(postBaseJpaEntity, tagJpaEntity));
        }

        postTagRepository.saveAll(newJpaEntities);
    }
}
