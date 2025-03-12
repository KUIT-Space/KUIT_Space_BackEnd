package space.space_spring.domain.post.application.port.in.Tag;

import space.space_spring.domain.discord.domain.DiscordTags;
import space.space_spring.domain.post.domain.Tag;

import java.util.List;
import java.util.Map;

public interface CreateTagUseCase {
    List<Tag> create(DiscordTags discordTags,Long boardId);
}
