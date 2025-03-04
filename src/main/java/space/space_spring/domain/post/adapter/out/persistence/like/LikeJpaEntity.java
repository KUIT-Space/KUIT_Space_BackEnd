package space.space_spring.domain.post.adapter.out.persistence.like;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Post_Like")

public class LikeJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="like_id")
    @NotNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity spaceMember;

    @Column(name="is_liked")
    @NotNull
    private boolean isLiked;    // 좋아요 여부

    private LikeJpaEntity(PostBaseJpaEntity postBase, SpaceMemberJpaEntity spaceMember, boolean isLiked) {
        this.postBase = postBase;
        this.spaceMember = spaceMember;
        this.isLiked = isLiked;
    }

    public static LikeJpaEntity create(PostBaseJpaEntity postBase, SpaceMemberJpaEntity spaceMember, boolean isLiked) {
        return new LikeJpaEntity(postBase, spaceMember, isLiked);
    }
}
