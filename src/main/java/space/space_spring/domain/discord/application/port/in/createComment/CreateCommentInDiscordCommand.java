package space.space_spring.domain.discord.application.port.in.createComment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCommentInDiscordCommand {
    private Long spaceId;
    private Long boardId;
    private Long originPostId;
    private String userName;
    private String profileUrl;
    private String content;

    //private final List<Tag> tags; //not yet


}
