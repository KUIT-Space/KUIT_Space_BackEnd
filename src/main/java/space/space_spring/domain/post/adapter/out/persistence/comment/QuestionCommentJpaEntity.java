package space.space_spring.domain.post.adapter.out.persistence.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.question.QuestionJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Question_Comment")
public class QuestionCommentJpaEntity {

    @Id
    @GeneratedValue
    @Column(name="question_comment_id")
    @NotNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_question_id")
    private QuestionJpaEntity question;

    private QuestionCommentJpaEntity(PostBaseJpaEntity postBase, QuestionJpaEntity question) {
        this.postBase = postBase;
        this.question = question;
    }

    public QuestionCommentJpaEntity create(PostBaseJpaEntity postBase, QuestionJpaEntity question) {
        return new QuestionCommentJpaEntity(postBase, question);
    }
}
