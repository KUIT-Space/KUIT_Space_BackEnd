package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Post_Comment")
@Getter
public class PostComment extends BaseEntity{
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
    private int like;

    @Column(name = "isReply")
    private boolean isReply;

    @Column(name = "comment_target_id")
    private String targetId;
}
