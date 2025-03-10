package space.space_spring.domain.post.application.port.out;

public interface LoadPostBasePort {
    Long loadByDiscordId(Long discordId);
}
