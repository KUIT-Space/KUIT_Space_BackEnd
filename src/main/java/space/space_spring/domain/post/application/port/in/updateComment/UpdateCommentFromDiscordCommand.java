package space.space_spring.domain.post.application.port.in.updateComment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateCommentFromDiscordCommand {
    private Long discordMessageId;
    private String content;

}
