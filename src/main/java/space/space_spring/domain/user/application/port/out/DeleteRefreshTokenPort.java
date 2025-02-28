package space.space_spring.domain.user.application.port.out;

public interface DeleteRefreshTokenPort {

    boolean deleteByUserId(Long userId);

}
