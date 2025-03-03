package space.space_spring.domain.discord.application.port.in.discord;

import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageInputFromDiscordCommand {
    private Long boardId;

    private String title;
    private String content;

    //private List<Tag> tag;
    private Long creatorDiscordId;
    private boolean isComment;
    private Long spaceDiscordId;
    private Long MessageDiscordId;

    @Override
    public String toString(){
        return "boardId:"+boardId+"\n"+
                "title:"+title+"\n"+
                "content:"+content+"\n"+
                "creatorDiscordId:"+creatorDiscordId+"\n"+
                "inComment:"+isComment+"\n"+
                "spaceDiscordId"+spaceDiscordId+"\n"+
                "MessageDiscordId"+MessageDiscordId;
    }
}
