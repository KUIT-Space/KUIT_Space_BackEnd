package space.space_spring.domain.user.adapter.out.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.user.application.port.out.CreateRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.DeleteRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.LoadRefreshTokenPort;

@RequiredArgsConstructor
@Repository
public class RefreshTokenPersistenceAdapter implements CreateRefreshTokenPort, LoadRefreshTokenPort, DeleteRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void create(Long userId, String refreshToken) {
        refreshTokenRepository.save(userId, refreshToken);
    }

    @Override
    public Optional<String> loadByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUserId(userId);
    }
}
