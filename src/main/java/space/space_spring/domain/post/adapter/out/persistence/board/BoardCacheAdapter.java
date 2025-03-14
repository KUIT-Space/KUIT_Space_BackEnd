package space.space_spring.domain.post.adapter.out.persistence.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.CreateBoardCachePort;
import space.space_spring.domain.post.application.port.out.DeleteBoardCachePort;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class BoardCacheAdapter implements CreateBoardCachePort, LoadBoardCachePort, DeleteBoardCachePort {
    private final StringRedisTemplate redisTemplate;

    private static final String CHANNEL_PREFIX = "channelId:";
    @Override
    public void create(Long discordId, Long id){
        redisTemplate.opsForValue().set(CHANNEL_PREFIX+ discordId, toString(id));
    }

    @Override
    public Optional<Long> findByDiscordId(Long discordId) {
        return Optional.ofNullable(toLong(redisTemplate.opsForValue().get(CHANNEL_PREFIX+ discordId)));
    }

    @Override
    public List<Long> findAllChannel(){
        return redisTemplate.keys(CHANNEL_PREFIX + "*").stream()
                .map(string->string.split(":")[1])
                .map(string->toLong(string)).toList();
    }

    public boolean deleteByDiscordId(Long discordId) {

        return Boolean.TRUE.equals(redisTemplate.delete(CHANNEL_PREFIX + discordId));
    }
    public int deleteAllChannel(){
        Set<String> keys =redisTemplate.keys(CHANNEL_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);  // 조회된 키 삭제

            System.out.println("Deleted keys: " + keys);
            return keys.size();
        } else {
            System.out.println("No matching keys found for pattern: channel:*");
            return 0;
        }
    }

    private String toString(Long value){
        if(value==null){
            return null;
        }
        return String.valueOf(value);
    }

    private Long toLong(String value){
        if(value==null){
            return null;
        }
        return Long.valueOf(value);
    }

}
