package space.space_spring.domain.user.application.port.out;

import java.util.Optional;

public interface LoadRefreshTokenPort {

    Optional<String> loadByUserId(Long userId);

}
