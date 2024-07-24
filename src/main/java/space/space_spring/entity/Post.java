package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Posts")
@Getter
public class Post extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "space_post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(name = "post_title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "post_type")
    private String type;

    @Column(name = "post_like")
    private int like;



}
