package space.space_spring.domain.user.application.port.out;

public interface CreateRefreshTokenPort {

    void create(Long userId, String refreshToken);

}
