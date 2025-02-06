package space.space_spring.domain.user.application.port.out;

import space.space_spring.domain.user.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> loadUser(Long userId);

    Optional<User> loadUserByDiscordId(Long discordId);
}
