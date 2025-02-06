package space.space_spring.domain.user.adapter.out.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.domain.user.domain.User;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> loadUserByDiscordId(Long discordId) {
        Optional<UserJpaEntity> userJpaEntity = userRepository.findByDiscordId(discordId);
        return userJpaEntity.map(userMapper::toDomainEntity);
    }
}
