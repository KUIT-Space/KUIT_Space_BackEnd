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
    private static final String REFRESH_TOKEN_KEY = "refreshToken";

    public void save(Long userId, String refreshToken) {
        redisTemplate.opsForHash().put(REFRESH_TOKEN_KEY, String.valueOf(userId), refreshToken);
        redisTemplate.expire(REFRESH_TOKEN_KEY, Duration.ofDays(14));
    }

    public Optional<String> findByUserId(Long userId) {
        return Optional.ofNullable((String) redisTemplate.opsForHash().get("refreshToken", String.valueOf(userId)));
    }

    public boolean deleteByUserId(Long userId) {
        return redisTemplate.opsForHash().delete(REFRESH_TOKEN_KEY, String.valueOf(userId)) > 0;
    }
}
