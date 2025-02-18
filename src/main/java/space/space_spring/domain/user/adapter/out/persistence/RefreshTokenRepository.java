package space.space_spring.domain.user.adapter.out.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "refreshToken:";

    public void save(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(PREFIX + userId, refreshToken, Duration.ofDays(14));
    }

    public Optional<String> findByUserId(Long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + userId));
    }

    public boolean deleteByUserId(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.delete(PREFIX + userId));
    }
}
