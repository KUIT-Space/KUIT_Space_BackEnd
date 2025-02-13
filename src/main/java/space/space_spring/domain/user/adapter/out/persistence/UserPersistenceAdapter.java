package space.space_spring.domain.user.adapter.out.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.user.domain.User;
import space.space_spring.domain.user.application.port.out.CreateUserPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;

@RequiredArgsConstructor
@Repository
public class UserPersistenceAdapter implements LoadUserPort, CreateUserPort {

    private final SpringDataUserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> loadUser(Long userId){
        return userRepository.findById(userId).map(userMapper::toDomainEntity);
    }

    @Override
    public Optional<User> loadUserByDiscordId(Long discordId){
        return userRepository.findByDiscordId(discordId).map(userMapper::toDomainEntity);
    }

    @Override
    public User createUser(User user){
        UserJpaEntity jpaUser = userMapper.toJpaEntity(user);
        UserJpaEntity result = userRepository.save(jpaUser);
        return userMapper.toDomainEntity(result);
    }

}
