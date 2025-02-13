package space.space_spring.domain.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.user.application.port.out.CreateRefreshTokenPort;

@RequiredArgsConstructor
@Repository
public class RefreshTokenPersistenceAdapter implements CreateRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void create(Long userId, String refreshToken) {
        refreshTokenRepository.save(userId, refreshToken);
    }
}
