package space.space_spring.domain.discord.application.port.in;

import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.Text;
@Builder
@Getter
public class CreatePostInDiscordCommand {
    private Long CreatorSpaceMemberId;
    private Long SpaceId;
    private Long BoardId;

    private String Title;
    private Text content;

}
