package space.space_spring.domain.space.application.port.out;

import space.space_spring.domain.space.domain.Space;

import java.util.Optional;

public interface LoadSpacePort {
    Optional<Space> loadSpaceByDiscordId(Long discordId);
    Optional<Space> loadSpaceById(Long Id);
}
