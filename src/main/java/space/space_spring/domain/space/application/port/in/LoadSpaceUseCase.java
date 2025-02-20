package space.space_spring.domain.space.application.port.in;

import space.space_spring.domain.space.domain.Space;

public interface LoadSpaceUseCase {
    Space load(Long spaceId);
    Space loadByDiscordId(Long DiscordId);
}
