package space.space_spring.domain.discord.application.port.in.discord;

import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageInputFromDiscordCommand {
    private Long boardId;
    private String rowContent;
    private Long creatorDiscordId;
    private boolean isComment;
    private Long spaceDiscordId;
    private Long MessageDiscordId;
}
