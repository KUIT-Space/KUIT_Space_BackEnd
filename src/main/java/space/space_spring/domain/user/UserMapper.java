package space.space_spring.domain.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToDomainEntity(UserJpaEntity jpaEntity) {
        return User.create(jpaEntity.getId(), jpaEntity.getDiscordId());
    }

    public UserJpaEntity mapToJpaEntity(User domain) {
        return UserJpaEntity.create(domain.getId(), domain.getDiscordId());
    }
}
