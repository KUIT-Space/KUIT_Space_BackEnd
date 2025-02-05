package space.space_spring.domain.user.adapter.out.persistence;

import org.springframework.stereotype.Component;
import space.space_spring.domain.user.domain.User;

@Component
public class UserMapper {

    public User mapToDomainEntity(UserJpaEntity jpaEntity) {
        return User.create(jpaEntity.getId(), jpaEntity.getDiscordId());
    }

    public UserJpaEntity mapToJpaEntity(User domain) {
        return UserJpaEntity.create(domain.getId(), domain.getDiscordId());
    }
}
