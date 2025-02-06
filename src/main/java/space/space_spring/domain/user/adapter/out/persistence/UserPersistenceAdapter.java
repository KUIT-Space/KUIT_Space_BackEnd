package space.space_spring.domain.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.user.User;
import space.space_spring.domain.user.UserJpaEntity;
import space.space_spring.domain.user.UserMapper;
import space.space_spring.domain.user.application.port.out.CreateUserPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserPersistenceAdapter implements LoadUserPort , CreateUserPort {
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
