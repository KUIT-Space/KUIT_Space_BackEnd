package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordMessageMapper {
    private final JDA jda;

    public MessageInputFromDiscordCommand mapToCommand(@NotNull MessageReceivedEvent event,Long boardId){
        String title="";
        String content="";
        String rowContent =event.getMessage().getContentRaw();
        boolean isComment=false;
        ChannelType channelType=event.getChannelType();
        if(event.isFromThread()){

            if(event.getMessage().getId().equals(event.getChannel().getId())){
                // Forum channel 에 첫 message
                title=event.getChannel().getName();
                content=rowContent;
                isComment=false;


            }else{
                // Thread 에 달린 댓글
                content=rowContent;
                isComment=true;

            }

        }

        if(channelType==ChannelType.TEXT){
            title = getFirstRow(rowContent,"\n");
            content = removeFirstRow(rowContent,"\n");
            isComment=false;


        }




        return MessageInputFromDiscordCommand.builder()
                .MessageDiscordId(event.getMessageIdLong())
                .isComment(isComment)
                .boardId(boardId)
                .creatorDiscordId(event.getMember().getIdLong())
                .spaceDiscordId(event.getGuild().getIdLong())
                .title(title)
                .content(content)
                .build();

    }

    public static String removeFirstRow(String input,String separator) {
        int index = input.indexOf(separator); // 첫 번째 "\n"의 위치 찾기
        return (index != -1) ? input.substring(index + 1) : input; // "\n" 이후 부분 반환
    }
    public static String getFirstRow(String input,String separator) {
        return input.split(separator)[0];
    }



}
