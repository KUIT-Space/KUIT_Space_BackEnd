package space.space_spring.domain.discord.application.port.in.discord;

import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import space.space_spring.domain.discord.adapter.in.discord.TitleAndContentParser;
import space.space_spring.domain.post.domain.AttachmentType;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class MessageInputFromDiscordCommand {
    private Long boardId;

    private String title;
    private String content;

    private List<Long> tagDiscordIds;
    private Long creatorDiscordId;
    private Long spaceDiscordId;
    private Long MessageDiscordId;
    private Map<String, AttachmentType> attachments;

    @Override
    public String toString(){
        return "boardId:"+boardId+"\n"+
                "title:"+title+"\n"+
                "content:"+content+"\n"+
                "creatorDiscordId:"+creatorDiscordId+"\n"+
                "spaceDiscordId"+spaceDiscordId+"\n"+
                "MessageDiscordId"+MessageDiscordId;
    }
//
//    @Builder
//    public MessageInputFromDiscordCommand(
//            Long boardId,
//            String title,
//            String content,
//
//            List<Long> tagDiscordIds,
//            Long creatorDiscordId,
//            boolean isComment,
//            Long spaceDiscordId,
//            Long MessageDiscordId,
//            Map<String, AttachmentType> attachments
//    ){
//        this.boardId=boardId;
//        this.title = title;
//        this.content=content;
//        this.tagDiscordIds = tagDiscordIds;
//        this.creatorDiscordId=creatorDiscordId;
//        this.isComment=isComment;
//        this.spaceDiscordId=spaceDiscordId;
//        this.MessageDiscordId=MessageDiscordId;
//        this.attachments=attachments;
//
//        this.rowContent = title+"\n"+content;
//    }
//
//    public static MessageInputFromDiscordCommand ofRowContent(
//            Long boardId,
//            String rowContent,
//
//            List<Long> tagDiscordIds,
//            Long creatorDiscordId,
//            boolean isComment,
//            Long spaceDiscordId,
//            Long MessageDiscordId,
//            Map<String, AttachmentType> attachments
//    ){
//        TitleAndContentParser parser = TitleAndContentParser.parser(rowContent);
//        return MessageInputFromDiscordCommand.builder()
//                .attachments(attachments)
//                .MessageDiscordId(MessageDiscordId)
//                .tagDiscordIds(tagDiscordIds)
//                .boardId(boardId)
//                .rowContent(rowContent)
//                .isComment(isComment)
//                .spaceDiscordId(spaceDiscordId)
//                .title(parser.getTitle())
//                .content(parser.getContent())
//                .build();
//    }

    public String getContentNotBlank(){
        if(content.isBlank()||content.isEmpty()){
            return title;
        }
        return content;
    }

    //content 혹은 title 이 20 자가 넘는지 확인 합니다.
    public boolean validateContentLength(){
        if(getContentNotBlank().length()>20){
            return true;
        }
        return false;
    }

}
