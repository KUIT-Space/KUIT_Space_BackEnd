package space.space_spring.domain.user.application.port.out;

import space.space_spring.domain.user.User;
import space.space_spring.domain.user.UserJpaEntity;

public interface CreateUserPort {

    User createUser(User user);
}
