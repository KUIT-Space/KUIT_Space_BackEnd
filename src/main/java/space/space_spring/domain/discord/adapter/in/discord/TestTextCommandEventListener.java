package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessageCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessagePort;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadPort;
import space.space_spring.domain.post.application.port.in.CreateBoard.CreateBoardCommand;
import space.space_spring.domain.space.application.port.in.CreateSpaceCommand;
import space.space_spring.domain.space.application.port.in.CreateSpaceUseCase;
import space.space_spring.domain.spaceMember.application.port.out.CreateSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.global.exception.CustomException;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TestTextCommandEventListener extends ListenerAdapter {
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreateDiscordMessagePort createDiscordMessagePort;
    private final CreateDiscordThreadPort createDiscordThreadPort;
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

        if(msg.getContentRaw().equals("!pingping")){
            CreateDiscordMessageCommand command=CreateDiscordMessageCommand.builder()
                            .channelDiscordId(event.getChannel().getIdLong())
                            .guildDiscordId(event.getGuild().getIdLong())
                            .WebHookUrl("https://discordapp.com/api/webhooks/1327500273425584250/_U2jXVFxmSq2XiFemb-nOn44PDMZs3KiX8ZXd2cF6mjRMO2D6QuN5qkLyxN37A3qSAr2")
                            .Content("spring server message test success")
                            .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                            .name(event.getMember().getEffectiveName())
                            .build();
            createDiscordMessagePort.send(command).thenAccept(result->{
                try {
                    event.getMessage().reply("success (id = "+result+")").queue();
                }catch (Exception e){
                    event.getMessage().reply("error : "+e.toString()).queue();
                }
            });
            return;
        }
        if(msg.getContentRaw().equals("!threadping")){
            CreateDiscordThreadCommand command=CreateDiscordThreadCommand.builder()
                    .channelDiscordId(event.getChannel().getIdLong())
                    //.guildDiscordId(event.getGuild().getIdLong())
                    .WebHookUrl("https://discordapp.com/api/webhooks/1327500273425584250/_U2jXVFxmSq2XiFemb-nOn44PDMZs3KiX8ZXd2cF6mjRMO2D6QuN5qkLyxN37A3qSAr2")
                    .contentMessage("spring server thread test success")
                    .threadName("test thread name 12")
                    .startMessage("start message 12")
                    .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                    .userName(event.getMember().getEffectiveName())

                    .build();
            createDiscordThreadPort.create(command).thenAccept(result->{
                try {
                    event.getMessage().reply("success (id = "+result+")").queue();
                }catch (Exception e){
                    event.getMessage().reply("error : "+e.toString()).queue();
                }
            });
            return;
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
