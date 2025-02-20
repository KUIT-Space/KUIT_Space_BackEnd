package space.space_spring.domain.discord.adapter.out;

import org.springframework.stereotype.Repository;
import space.space_spring.domain.discord.application.port.out.createPost.CreatePostMessageCommand;
import space.space_spring.domain.discord.application.port.out.createPost.CreatePostMessagePort;

@Repository
public class DiscordPostAdapter implements CreatePostMessagePort {

    @Override
    public Long createPostMessage(CreatePostMessageCommand command) {
        return 0L;
        // 상준씨 여기도 구현해주세여
    }
}
