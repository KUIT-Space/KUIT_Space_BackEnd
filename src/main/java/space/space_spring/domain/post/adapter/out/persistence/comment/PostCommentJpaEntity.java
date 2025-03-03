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

    private PostCommentJpaEntity(PostBaseJpaEntity postBase, PostJpaEntity post) {
        this.postBase = postBase;
        this.post = post;
    }

    public PostCommentJpaEntity create(PostBaseJpaEntity postBase, PostJpaEntity post) {
        return new PostCommentJpaEntity(postBase, post);
    }
}
