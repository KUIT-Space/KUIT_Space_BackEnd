package space.space_spring.domain.post.adapter.out.persistence.like;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Post_Like")

public class LikeJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue
    @Column(name="like_id")
    @NotNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    private LikeJpaEntity(PostBaseJpaEntity postBase) {
        this.postBase = postBase;
    }

    public static LikeJpaEntity create(PostBaseJpaEntity postBase) {
        return new LikeJpaEntity(postBase);
    }
}
