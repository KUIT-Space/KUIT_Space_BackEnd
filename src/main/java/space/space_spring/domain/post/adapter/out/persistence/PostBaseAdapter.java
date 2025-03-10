package space.space_spring.domain.post.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.application.port.out.LoadPostBasePort;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@Repository
@RequiredArgsConstructor
public class PostBaseAdapter implements LoadPostBasePort {

    private final SpringDataPostBaseRepository postBaseRepository;
    @Override
    public Long loadByDiscordId(Long discordId){
        return postBaseRepository.findByDiscordIdAndStatus(discordId, BaseStatusType.ACTIVE).orElse(null).getId();
    }
}
