package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
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
import java.util.List;

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
            //event.getChannel().asTextChannel().sendMessage().set
            msg.reply("pong").queue();
            return;
        }
        if(msg.getContentRaw().equals("!webHook")){
            List<Webhook> hookList=event.getChannel().asTextChannel().retrieveWebhooks().complete();
            for(Webhook hook:hookList){
                System.out.println("\n"+hook.getName()+"\n");
            }


            Webhook webHook =event.getChannel().asTextChannel().createWebhook("generate WebHook").complete();
            webHook.sendMessage("only webhook Message").setAvatarUrl(event.getAuthor().getAvatarUrl()).setUsername(event.getMember().getEffectiveName()).queue();
            //event.getChannel().asTextChannel().retrieveWebhooks()

            //WebhookClient.createClient().get
            WebhookClient.createClient(event.getJDA(),webHook.getUrl()).sendMessage("webHook message")
                    .setAvatarUrl(event.getAuthor().getAvatarUrl()).setUsername(event.getMember().getEffectiveName()).queue();
            return;
        }
        if(msg.getContentRaw().equals("!pingping")){
            CreateDiscordMessageCommand command=CreateDiscordMessageCommand.builder()
                            .channelDiscordId(event.getChannel().getIdLong())
                            .guildDiscordId(event.getGuild().getIdLong())
                            .webHookUrl("https://discordapp.com/api/webhooks/1327500273425584250/_U2jXVFxmSq2XiFemb-nOn44PDMZs3KiX8ZXd2cF6mjRMO2D6QuN5qkLyxN37A3qSAr2")
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
                    .webHookUrl("https://discordapp.com/api/webhooks/1327500273425584250/_U2jXVFxmSq2XiFemb-nOn44PDMZs3KiX8ZXd2cF6mjRMO2D6QuN5qkLyxN37A3qSAr2")
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

        if(msg.getContentRaw().equals("!forumping")) {
            CreateDiscordMessageCommand command=CreateDiscordMessageCommand.builder()
                    .channelDiscordId(event.getChannel().getIdLong())
                    .guildDiscordId(event.getGuild().getIdLong())
                    .webHookUrl("https://discordapp.com/api/webhooks/1341258654082404403/SnSx0qzymTEkwuEeVfXMPsUbSj_yiQ0tlCSOX4WOalSKBrdDlBXbz_TMFqnWIyIBy60m")
                    .Content("spring server message test success\nhi\ncontent")
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

        if(msg.getContentRaw().startsWith("!member:")){
            System.out.println("\n\n\nmembertext\n\n\n");
            System.out.println("mem:"+msg.getGuild().getMembers().size());
            Long spaceId= Long.parseLong(msg.getContentRaw().split(":")[1]);
            loadSpaceMemberPort.loadSpaceMemberBySpaceId(spaceId).stream().forEach(spaceMember->{
                msg.getChannel().sendMessage("\n"+spaceMember.getNickname()+":"+spaceMember.getId()).queue();
            });
            return;
        }

        if(msg.getContentRaw().equals("!mention")) {


            event.getChannel().sendMessage("hi"+event.getMember().getAsMention()).queue();
        }
    }


}
