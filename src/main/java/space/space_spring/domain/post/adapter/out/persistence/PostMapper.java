package space.space_spring.domain.post.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.domain.Post;

@Component
@RequiredArgsConstructor
public class PostMapper {

    PostJpaEntity toJpaEntity(PostBaseJpaEntity postBase, Post domain) {
        return PostJpaEntity.create(
                postBase,
                domain.getTitle()
        );
    }
}
