package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.space.application.port.in.CreateSpaceCommand;
import space.space_spring.domain.space.application.port.in.CreateSpaceUseCase;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.global.exception.CustomException;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TestTextCommandEventListener extends ListenerAdapter {
    private final LoadSpaceMemberPort loadSpaceMemberPort;

    //private final CreateSpaceUseCase createSpaceUseCase;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){

        Message msg = event.getMessage();
        if(msg.getAuthor().isBot()){
            return;
        }

        if(msg.getContentRaw().equals("!ping")){
            msg.reply("pong").queue();
            return;
        }
        if(msg.getContentRaw().equals("!init-space")){
            //이 길드를 space로 등록합니다
            Guild guild = event.getGuild();
            CreateSpaceCommand command = CreateSpaceCommand.builder()
                    .guildId(guild.getIdLong())
                    .guildName(guild.getName())
                    .build();

//            try{Long spaceId = createSpaceUseCase.createSpace(command);
//                event.getMessage()
//                        .reply("space setting success!\nspace ID : "+spaceId+"\n").queue();
//            }
//            catch (CustomException e){
//            /*
//             * 이미 guild가 space로 등록이 된 경우
//             * */
//                event.getMessage().reply(e.getMessage()).queue();
//                return;
//            }

        }
        if(msg.getContentRaw().startsWith("!member:")){
            System.out.println("\n\n\nmembertext\n\n\n");
            System.out.println("mem:"+msg.getGuild().getMembers().size());
            Long spaceId= Long.parseLong(msg.getContentRaw().split(":")[1]);
            loadSpaceMemberPort.loadSpaceMemberBySpaceId(spaceId).stream().forEach(spaceMember->{
                msg.getChannel().sendMessage("\n"+spaceMember.getNickname()+":"+spaceMember.getId()).queue();
            });
            return;
        }
    }


}
