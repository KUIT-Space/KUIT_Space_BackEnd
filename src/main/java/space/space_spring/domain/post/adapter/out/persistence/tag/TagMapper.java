package space.space_spring.domain.post.adapter.out.persistence.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.domain.Tag;

@Component
@RequiredArgsConstructor
public class TagMapper {

//    public TagJpaEntity toJpaEntity() {
//    }

    public Tag toDomainEntity(TagJpaEntity tagJpaEntity) {
        return Tag.create(tagJpaEntity.getId(), tagJpaEntity.getDiscordId(), tagJpaEntity.getTagName(), tagJpaEntity.getBoard().getId());
    }
}
