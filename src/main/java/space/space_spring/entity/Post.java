package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Posts")
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(name = "title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "type")
    private String type;

    @Column(name = "post_like")
    private int likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> Comments;

    @Builder
    public Post(User user, Space space, String title, String content, String type, List<PostImage> postImages) {
        this.user = user;
        this.space = space;
        this.title = title;
        this.content = content;
        this.type = type;
        this.postImages = postImages;
    }

    // 좋아요 개수 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 개수 감소
    public void decreaseLikeCount() {
        this.likeCount--;
    }

    // 게시글 수정
    public void updatePost(String title, String content, List<PostImage> postImages) {
        this.title = title;
        this.content = content;
        this.postImages.clear();
        this.postImages.addAll(postImages);
    }
}
