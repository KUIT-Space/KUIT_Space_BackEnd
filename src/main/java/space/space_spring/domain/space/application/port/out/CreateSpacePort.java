package space.space_spring.domain.space.application.port.out;

import space.space_spring.domain.space.domain.Space;

public interface CreateSpacePort {

    Space saveSpace(Long discordGuildId, String spaceName);
}
