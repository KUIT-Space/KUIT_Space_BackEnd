package space.space_spring.domain.post.adapter.out.persistence.like;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
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

    @Column(name="is_liked")
    @NotNull
    private boolean isLiked;    // 좋아요 여부

    private LikeJpaEntity(PostBaseJpaEntity postBase, boolean isLiked) {
        this.postBase = postBase;
        this.isLiked = isLiked;
    }

    public static LikeJpaEntity create(PostBaseJpaEntity postBase, boolean isLiked) {
        return new LikeJpaEntity(postBase, isLiked);
    }
}
