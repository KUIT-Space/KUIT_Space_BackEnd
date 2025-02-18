package space.space_spring.domain.user.application.port.out;

import space.space_spring.domain.user.domain.User;

public interface CreateUserPort {

    User createUser(User user);
}
