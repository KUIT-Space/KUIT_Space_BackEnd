package space.space_spring.domain.post.adapter.out.persistence.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.CreateBoardCachePort;
import space.space_spring.domain.post.application.port.out.DeleteBoardCachePort;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardCacheAdapter implements CreateBoardCachePort, LoadBoardCachePort, DeleteBoardCachePort {
    private final RedisTemplate redisTemplate;

    private static final String CHANNEL_PREFIX = "channelId:";
    @Override
    public void create(Long discordId, Long id){
        redisTemplate.opsForValue().set(CHANNEL_PREFIX+ discordId, id);
    }

    @Override
    public Optional<Long> findByDiscordId(Long discordId) {
        return Optional.ofNullable((Long)redisTemplate.opsForValue().get(CHANNEL_PREFIX+ discordId));
    }

    public boolean deleteByDiscordId(Long discordId) {

        return Boolean.TRUE.equals(redisTemplate.delete(CHANNEL_PREFIX + discordId));
    }

}
