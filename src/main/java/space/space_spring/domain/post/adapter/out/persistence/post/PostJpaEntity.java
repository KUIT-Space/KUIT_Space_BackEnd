package space.space_spring.domain.post.adapter.out.persistence.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Post")
public class PostJpaEntity {

    @Id
    @GeneratedValue
    @Column(name="post_id")
    @NotNull
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntqity postBase;

    private String title;

    private PostJpaEntity(PostBaseJpaEntity postBase, String title) {
        this.postBase = postBase;
        this.title = title;
    }

    public static PostJpaEntity create(PostBaseJpaEntity postBase,String title) {
        return new PostJpaEntity(postBase, title);
    }
}
