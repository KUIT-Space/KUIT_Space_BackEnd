package space.space_spring.domain.discord.application.port.in.discord;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.AttachmentType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class CommentInputFromDiscordCommand {
    private Long boardId;

    private String content;

    private Long creatorDiscordId;
    private Long spaceDiscordId;
    private Long MessageDiscordId;
    private Map<String, AttachmentType> attachments;

    private OffsetDateTime createdAt;
    private OffsetDateTime lastModifiedAt;

    @Override
    public String toString(){
        return "boardId:"+boardId+"\n"+
                "content:"+content+"\n"+
                "creatorDiscordId:"+creatorDiscordId+"\n"+
                "spaceDiscordId"+spaceDiscordId+"\n"+
                "MessageDiscordId"+MessageDiscordId;
    }





}
