package space.space_spring.domain.post.adapter.out.persistence.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.application.port.out.like.ChangeLikeStatePort;
import space.space_spring.domain.post.application.port.out.like.CreateLikePort;
import space.space_spring.domain.post.application.port.out.like.LoadLikePort;
import space.space_spring.domain.post.domain.Like;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class LikePersistenceAdapter implements LoadLikePort, CreateLikePort, ChangeLikeStatePort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataLikeRepository likeRepository;
    private final LikeMapper likeMapper;

    @Override
    public Map<Long, NaturalNumber> countLikesByPostIds(List<Long> postIds) {
        List<PostLikeCount> results = likeRepository.countLikesByPostIds(postIds, BaseStatusType.ACTIVE);
        return results.stream()
                .collect(Collectors.toMap(
                        PostLikeCount::getPostId,
                        postLikeCount -> NaturalNumber.of(postLikeCount.getLikeCount().intValue())
                ));
    }

    @Override
    public NaturalNumber countLikeByPostId(Long postId) {
        int likeCountOfTarget = likeRepository.countByPostBaseIdAndLikedAndStatus(postId, true, BaseStatusType.ACTIVE);
        return NaturalNumber.of(likeCountOfTarget);
    }

    @Override
    public boolean hasSpaceMemberLiked(Long spaceMemberId, Long targetId) {
        Optional<LikeJpaEntity> likeJpaEntity = likeRepository.findBySpaceMemberIdAndPostBaseIdAndStatus(spaceMemberId, targetId, BaseStatusType.ACTIVE);

        return likeJpaEntity.isPresent();
    }

    @Override
    public void changeLikeState(Long spaceMemberId, Long targetId, boolean changeTo) {
        LikeJpaEntity likeJpaEntity = likeRepository.findBySpaceMemberIdAndPostBaseIdAndStatus(spaceMemberId, targetId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(LIKE_NOT_FOUND));

        likeJpaEntity.changeLikeState(changeTo);
    }

    @Override
    public Like createLike(Like like) {
        PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findByIdAndStatus(like.getTargetId(), BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findByIdAndStatus(like.getSpaceMemberId(), BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        LikeJpaEntity save = likeRepository.save(likeMapper.toJpaEntity(postBaseJpaEntity, spaceMemberJpaEntity, like));
        return likeMapper.toDomainEntity(save);
    }
}
