package space.space_spring.domain.post.adapter.out.persistence.question;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Question")
public class QuestionJpaEntity {

    @Id
    @GeneratedValue
    @Column(name="question_id")
    @NotNull
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    private String title;

    @Column(name = "is_anonymous")
    @NotNull
    private boolean isAnonymous;

    private QuestionJpaEntity(PostBaseJpaEntity postBase, String title, boolean isAnonymous) {
        this.postBase = postBase;
        this.title = title;
        this.isAnonymous = isAnonymous;
    }

    public static QuestionJpaEntity create(PostBaseJpaEntity postBase, String title, boolean isAnonymous) {
        return new QuestionJpaEntity(postBase, title, isAnonymous);
    }
}
