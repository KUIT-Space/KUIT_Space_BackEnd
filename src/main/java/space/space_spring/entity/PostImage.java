package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Post_Image")
@Getter
public class PostImage extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "post_image_id")
    private Long postImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_post_id")
    private Post post;

    @Column(name = "post_image_url")
    private String postImgUrl;

}
