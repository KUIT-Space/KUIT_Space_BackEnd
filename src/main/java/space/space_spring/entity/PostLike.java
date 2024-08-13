package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Post_Like")
@Getter
@NoArgsConstructor
public class PostLike extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "post_like_id")
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
