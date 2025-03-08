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

    /**
     * Comment 테이블 하나로 합치면 수정해야함 -> 익명 여부 추가
     */

    @Id
    @GeneratedValue
    @Column(name="post_comment_id")
    @NotNull
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
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
