package space.space_spring.domain.post.adapter.out.persistence.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Post_Comment")
public class PostCommentJpaEntity {

    @Id
    @Column(name="post_comment_id")
    @NotNull
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId     // PostBaseJpaEntity의 PK 를 공유
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    @NotNull
    private PostJpaEntity post;

    @NotNull
    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    private PostCommentJpaEntity(PostBaseJpaEntity postBase, PostJpaEntity post, boolean isAnonymous) {
        this.id = postBase.getId();     // PostBaseJpaEntity 의 식별자가 이미 있어야 한다
        this.postBase = postBase;
        this.post = post;
        this.isAnonymous = isAnonymous;
    }

    public static PostCommentJpaEntity create(PostBaseJpaEntity postBase, PostJpaEntity post, boolean isAnonymous) {
        return new PostCommentJpaEntity(postBase, post, isAnonymous);
    }

    public void changeAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
