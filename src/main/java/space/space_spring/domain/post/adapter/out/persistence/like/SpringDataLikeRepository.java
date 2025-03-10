package space.space_spring.domain.post.adapter.out.persistence.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;
import java.util.Optional;

public interface SpringDataLikeRepository extends JpaRepository<LikeJpaEntity, Long> {

    @Query("SELECT new space.space_spring.domain.post.adapter.out.persistence.like.PostLikeCount(l.postBase.id, COUNT(l)) " +
            "FROM LikeJpaEntity l " +
            "WHERE l.postBase.id IN :postIds " +
            "AND l.isLiked = true " +
            "AND l.status = space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE " +
            "GROUP BY l.postBase.id")
    List<PostLikeCount> countLikesByPostIds(@Param("postIds") List<Long> postIds);

    /**
     * findBySpaceMemberIdAndPostBaseIdAndStatus(Long spaceMemberId, Long postBaseId, BaseStatusType status),
     * findBySpaceMember_IdAndPostBase_IdAndStatus(Long spaceMemberId, Long postBaseId, BaseStatusType status),
     * findBySpaceMemberAndPostBaseAndStatus(SpaceMemberJpaEntity spaceMemberJpa, PostBaseJpaEntity postBaseJpa, BaseStatusType status)
     * -> 이렇게 3가지 모두 가능
     */
    Optional<LikeJpaEntity> findBySpaceMemberIdAndPostBaseIdAndStatus(Long spaceMemberId, Long postBaseId, BaseStatusType status);

    int countByPostBaseIdAndLikedAndStatus(Long targetId, boolean isLiked, BaseStatusType status);
}
