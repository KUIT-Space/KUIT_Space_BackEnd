package space.space_spring.domain.user.application.port.out;

import java.util.Optional;
import space.space_spring.domain.user.domain.User;

public interface LoadUserPort {

    Optional<User> loadUserByDiscordId(Long discordId);

    User saveUser(User user);

}
