package space.space_spring.domain.space.application.port.out;

import space.space_spring.domain.space.domain.Space;

public interface LoadSpacePort {
    Space loadSpaceByDiscordId(Long discordId);
}
