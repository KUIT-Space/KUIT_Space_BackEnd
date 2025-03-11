package space.space_spring.domain.post.adapter.out.persistence.postTag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Post_Tag")
public class PostTagJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_tag_id")
    @NotNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    @NotNull
    private TagJpaEntity tag;

    @ManyToOne
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    private PostTagJpaEntity(PostBaseJpaEntity postBase, TagJpaEntity tag) {
        this.postBase = postBase;
        this.tag = tag;
    }

    public static PostTagJpaEntity create(PostBaseJpaEntity postBase, TagJpaEntity tag) {
        return new PostTagJpaEntity(postBase, tag);
    }
}
