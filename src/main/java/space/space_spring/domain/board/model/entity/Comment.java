package space.space_spring.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.BaseEntity;

@Entity
@Table(name = "Post_Comment")
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment_content")
    private String content;

    @Column(name = "comment_like")
    private int likeCount;


    @Column(name = "comment_target_id")
    private Long targetId;

    @Builder
    public Comment(User user, Post post, String content, Boolean isReply, Long targetId) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.targetId = targetId;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
