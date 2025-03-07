package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.*;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.global.exception.CustomException;

import java.util.List;

@Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
public class TestTextCommandEventListener extends ListenerAdapter {
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreateDiscordWebHookMessagePort createDiscordWebHookMessagePort;
    private final CreateDiscordThreadPort createDiscordThreadPort;
    //private final CreateSpaceUseCase createSpaceUseCase;
    private final CreateDiscordMessageOnThreadPort createDiscordMessageOnThreadPort;
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
            CreateDiscordWebHookMessageCommand command= CreateDiscordWebHookMessageCommand.builder()
                            .channelDiscordId(event.getChannel().getIdLong())
                            .guildDiscordId(event.getGuild().getIdLong())
                            .webHookUrl("https://discordapp.com/api/webhooks/1327500273425584250/_U2jXVFxmSq2XiFemb-nOn44PDMZs3KiX8ZXd2cF6mjRMO2D6QuN5qkLyxN37A3qSAr2")
                            .content("spring server message test success")
                            .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                            .name(event.getMember().getEffectiveName())
                            .build();
            createDiscordWebHookMessagePort.send(command).thenAccept(result->{
                try {
                    event.getMessage().reply("success (id = "+result+")").queue();
                }catch (Exception e){
                    event.getMessage().reply("error : "+e.toString()).queue();
                }
            });
            return;
        }
        if(msg.getContentRaw().equals("!threadping")){
            Webhook webhook= event.getChannel().asTextChannel().createWebhook("made by server").complete();
            CreateDiscordThreadCommand command=CreateDiscordThreadCommand.builder()
                    .channelDiscordId(event.getChannel().getIdLong())
                    .guildDiscordId(event.getGuild().getIdLong())
                    .webHookUrl(webhook.getUrl())
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
            }).exceptionally(t->{
                System.out.println("\n\nerror : "+t.toString());
                return null;
            });
            return;
        }

        if(msg.getContentRaw().equals("!forumping")) {
            CreateDiscordThreadCommand command=CreateDiscordThreadCommand.builder()
                    .channelDiscordId(1326507142286544957L)
                    .guildDiscordId(event.getGuild().getIdLong())
                    .webHookUrl("https://discordapp.com/api/webhooks/1341258654082404403/SnSx0qzymTEkwuEeVfXMPsUbSj_yiQ0tlCSOX4WOalSKBrdDlBXbz_TMFqnWIyIBy60m")
                    .contentMessage("spring server message test success\nhi\ncontent")
                    .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                    .userName(event.getMember().getEffectiveName())
                    .threadName("thread name yee")
                    .build();
            createDiscordThreadPort.create(command).thenAccept(result->{
                try {
                    event.getMessage().reply("success (id = "+result+")"+"\nchannel ID : "+event.getChannel().getId()).queue();
                }catch (Exception e){
                    event.getMessage().reply("error : "+e.toString()).queue();
                }
            }).exceptionally(t->{
                System.out.println("\n\nerror : "+t.toString());
                return null;
            });

            return;

        }

        if(msg.getContentRaw().equals("!send")){
            CreateDiscordWebHookMessageCommand command = CreateDiscordWebHookMessageCommand.builder()
                    .title("message")
                    .content("content")
                    .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                    .webHookUrl(event.getChannel().asTextChannel().createWebhook("space").complete().getUrl())
                    .guildDiscordId(event.getGuild().getIdLong())
                    .channelDiscordId(event.getChannel().getIdLong())
                    .name(event.getMember().getEffectiveName())
                    .build();
            createDiscordWebHookMessagePort.send(command).thenAccept(result->{
                try {
                    event.getMessage().reply("success \n(id = "+result+")"+"\nchannel ID : "+event.getChannel().getId()).queue();
                }catch (Exception e){
                    event.getMessage().reply("error : "+e.toString()).queue();
                }
            }).exceptionally(t->{
                System.out.println("\n\nerror : "+t.toString());
                return null;
            });
            return;
        }

        if(msg.getContentRaw().startsWith("!comment:")){
            String[] commands = msg.getContentRaw().split(":");
            Long msgId = Long.parseLong(commands[1]);
            Long channelId = Long.parseLong(commands[2]);

            CreateDiscordMessageOnThreadCommand command = CreateDiscordMessageOnThreadCommand.builder()
                    .originPostId(0L)
                    .originChannelId(channelId)
                    .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                    .webHookUrl(
                            event.getGuild().getGuildChannelById(channelId).getType()== ChannelType.TEXT ?
                            event.getGuild().getChannelById(TextChannel.class,channelId).createWebhook("space").complete().getUrl()
                            :event.getGuild().getChannelById(ForumChannel.class,channelId).createWebhook("space").complete().getUrl())
                    .userName(event.getMember().getEffectiveName())
                    .threadChannelDiscordId(msgId )
                    .content("reply success")
                    .guildDiscordId(event.getGuild().getIdLong())
                    .build();
            createDiscordMessageOnThreadPort.sendToThread(command).thenAccept(result->{
                try {
                    event.getMessage().reply("success \n(id = "+result+")"+"\nchannel ID : "+event.getChannel().getId()).queue();
                }catch (Exception e){
                    event.getMessage().reply("error : "+e.toString()).queue();
                }
            }).exceptionally(t->{
                System.out.println("\n\nerror : "+t.toString());
                return null;
            });

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
        if(msg.getContentRaw().equals("!addTag")) {

            if(event.getChannelType().isThread()){
                List<ForumTag> tags = event.getChannel().asThreadChannel().getParentChannel().asForumChannel().getAvailableTags();
                event.getChannel().asThreadChannel().getManager().setAppliedTags(tags).queue();
            }

        }
    }


}
