package space.space_spring.domain.post.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Comment")
public class CommentJpaEntity {

    @Id
    @GeneratedValue
    @Column(name="comment_id")
    @NotNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostJpaEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionJpaEntity question;

    private CommentJpaEntity(PostBaseJpaEntity postBase, PostJpaEntity post, QuestionJpaEntity question) {
        this.postBase = postBase;
        this.post = post;
        this.question = question;
    }

    public CommentJpaEntity create(PostBaseJpaEntity postBase, PostJpaEntity post, QuestionJpaEntity question) {
        return new CommentJpaEntity(postBase, post, question);
    }
}
