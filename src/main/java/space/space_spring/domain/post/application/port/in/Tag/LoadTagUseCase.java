package space.space_spring.domain.post.application.port.in.Tag;

import space.space_spring.domain.post.domain.Tag;

import java.util.List;

public interface LoadTagUseCase {
    List<Tag> findByDiscordId(List<Long> tagDiscordId);
}
