package space.space_spring.domain.post.application.port.out;

public interface DeleteBoardCachePort {
    boolean deleteByDiscordId(Long discordId);
}
