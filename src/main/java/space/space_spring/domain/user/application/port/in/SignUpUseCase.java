package space.space_spring.domain.user.application.port.in;

import space.space_spring.domain.user.domain.User;

public interface SignUpUseCase {

    boolean signUp(User user);

}
