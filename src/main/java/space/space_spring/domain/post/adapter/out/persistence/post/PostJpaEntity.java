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
    @Column(name="post_id")
    @NotNull
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId     // PostBaseJpaEntity의 PK 를 공유
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    @NotNull
    private String title;

    @Column(name = "is_anonymous")
    @NotNull
    private Boolean isAnonymous;

    private PostJpaEntity(PostBaseJpaEntity postBase, String title, Boolean isAnonymous) {
        //this.id = postBase.getId();     // PostBaseJpaEntity 의 식별자가 이미 있어야 한다
        this.postBase = postBase;
        this.title = title;
        this.isAnonymous = isAnonymous;
    }

    public static PostJpaEntity create(PostBaseJpaEntity postBase, String title, Boolean isAnonymous) {
        return new PostJpaEntity(postBase, title, isAnonymous);
    }
}
