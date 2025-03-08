package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.application.port.in.CreateSpaceCommand;
import space.space_spring.domain.space.application.port.in.CreateSpaceUseCase;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.global.exception.CustomException;

import java.util.Arrays;

@Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
public class TestTextCommandEventListener extends ListenerAdapter {
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreateBoardUseCase createBoardUseCase;
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

        if(msg.getContentRaw().startsWith("!member:")){
            System.out.println("\n\n\nmembertext\n\n\n");
            System.out.println("mem:"+msg.getGuild().getMembers().size());
            Long spaceId= Long.parseLong(msg.getContentRaw().split(":")[1]);
            loadSpaceMemberPort.loadSpaceMemberBySpaceId(spaceId).stream().forEach(spaceMember->{
                msg.getChannel().sendMessage(
                        "\n"+spaceMember.getNickname()+":"+spaceMember.getId()+":Manager:"+spaceMember.isManager()).queue();
            });
            return;
        }

        if(msg.getContentRaw().equals("!createChannel")){
            MessageChannelUnion channel= event.getChannel();
            CreateBoardCommand command =CreateBoardCommand.builder()
                    .boardType(BoardType.POST)
                    .boardName(channel.getName())
                    .discordId(channel.getIdLong())
                    .webhookUrl("https://discordapp.com/api/webhooks/1327265824947310612/9SZPFqGg7fjtYAMvwkTzEtirIMWSKyDegigxupEovyiE_hJgrlOWvt2HO8O3GUM7evoV")
                    .spaceId(1L)
                    .build();
            createBoardUseCase.createBoard(command);
            return;
        }
    }


}
