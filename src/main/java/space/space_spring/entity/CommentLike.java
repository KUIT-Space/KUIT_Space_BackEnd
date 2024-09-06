package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Comment_Like",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_comment_id"})})
@Getter
@NoArgsConstructor
public class CommentLike extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "comment_like_id")
    private Long commentLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
