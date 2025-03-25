package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.in.discord.CommentInputFromDiscordCommand;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;
import space.space_spring.domain.post.domain.AttachmentType;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordMessageMapper {
    public MessageInputFromDiscordCommand mapToPostCommandFromText(@NotNull Message message,Long boardId){

        TitleAndContentParser parser = TitleAndContentParser.parse(message.getContentRaw());

        return MessageInputFromDiscordCommand.builder()
                .MessageDiscordId(message.getIdLong())
                .boardId(boardId)
                .creatorDiscordId(message.getMember().getIdLong())
                .spaceDiscordId(message.getGuild().getIdLong())
                .title(parser.getTitle())
                .tagDiscordIds(List.of())
                .content(parser.getContent())
                .attachments(
                        getAttachments(message))
                .build();
    }
    public MessageInputFromDiscordCommand mapToPostCommandFromForum(@NotNull Message message,Long boardId){
        String title=message.getChannel().getName();
        String content = message.getContentRaw();

        List<Long> tagDiscordIds= new ArrayList();
        tagDiscordIds.addAll( message.getChannel().asThreadChannel().getAppliedTags().stream().map(
                tag->{return tag.getIdLong();}
        ).toList());
        return MessageInputFromDiscordCommand.builder()
                .MessageDiscordId(message.getIdLong())
                .boardId(boardId)
                .creatorDiscordId(message.getMember().getIdLong())
                .spaceDiscordId(message.getGuild().getIdLong())
                .title(title)
                .tagDiscordIds(tagDiscordIds)
                .content(content)

                .attachments(
                        getAttachments(message))
                .createdAt(message.getTimeCreated())
                .lastModifiedAt(message.getTimeEdited())
                .build();
    }



    public CommentInputFromDiscordCommand mapToCommentCommand(@NotNull Message message,Long boardId){
        return CommentInputFromDiscordCommand.builder()
                .attachments(getAttachments(message))
                .MessageDiscordId(message.getIdLong())
                .boardId(boardId)
                .content(message.getContentRaw())
                .createdAt(message.getTimeCreated())
                .creatorDiscordId(message.getMember().getIdLong())
                .lastModifiedAt(message.getTimeEdited())
                .spaceDiscordId(message.getGuild().getIdLong())
                .build();
    }


    public static String removeFirstRow(String input,String separator) {
        int index = input.indexOf(separator); // 첫 번째 "\n"의 위치 찾기
        return (index != -1) ? input.substring(index + 1) : input; // "\n" 이후 부분 반환
    }
    public static String getFirstRow(String input,String separator) {
        return input.split(separator)[0];
    }

    public static Map<String, AttachmentType> getAttachments(Message message){
        if(message.getAttachments()==null){
            return Map.of();
        }
        Map<String,AttachmentType> attachments = new HashMap<>();
        message.getAttachments().stream().forEach(att->{
            attachments.put(att.getUrl(),getAttachmentType(att.getContentType()));
        });
        return attachments;
    }

    public static AttachmentType getAttachmentType(String type){
        if(type.contains("image")){
            return AttachmentType.IMAGE;
        }
        return AttachmentType.FILE;
    }

}
