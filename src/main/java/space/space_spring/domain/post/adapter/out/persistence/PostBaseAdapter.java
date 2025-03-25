package space.space_spring.domain.post.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.application.port.out.LoadPostBasePort;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostBaseAdapter implements LoadPostBasePort {

    private final SpringDataPostBaseRepository postBaseRepository;
    @Override
    public Optional<Long> loadByDiscordId(Long discordId){
        PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findByDiscordIdAndStatus(discordId, BaseStatusType.ACTIVE)
                .orElse(null);
        return Optional.of(postBaseJpaEntity.getId());
    }
}
