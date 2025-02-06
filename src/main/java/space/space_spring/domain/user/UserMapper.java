package space.space_spring.domain.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomainEntity(UserJpaEntity jpaEntity) {
        return User.create(jpaEntity.getId(), jpaEntity.getDiscordId());
    }

    public UserJpaEntity toJpaEntity(User domain) {
        return UserJpaEntity.create(domain.getId(), domain.getDiscordId());
    }
}
