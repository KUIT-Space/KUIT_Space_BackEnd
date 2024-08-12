package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Post_Image")
@Getter
@NoArgsConstructor
public class PostImage extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "post_image_id")
    private Long postImgId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_post_id")
    private Post post;

    @Column(name = "post_image_url")
    private String postImgUrl;

    @Builder
    public PostImage(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

}
