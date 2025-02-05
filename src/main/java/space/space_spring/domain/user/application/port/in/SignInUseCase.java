package space.space_spring.domain.user.application.port.in;

import space.space_spring.domain.user.domain.User;

public interface SignInUseCase {

    boolean userExist(User user);

    boolean signIn(User user);

}
