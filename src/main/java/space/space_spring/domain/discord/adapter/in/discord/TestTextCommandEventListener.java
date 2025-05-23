package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import space.space_spring.domain.discord.adapter.out.EditDiscordMessage.EditDiscordMessageAdapter;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordUseCase;
import space.space_spring.domain.discord.application.port.out.*;

import space.space_spring.domain.discord.application.port.out.deleteWebHookMessage.DeleteDiscordWebHookMessagePort;
import space.space_spring.domain.discord.application.port.out.updateMessage.UpdateMessageInDiscordPort;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentCommand;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.application.port.in.CreateSpaceCommand;
import space.space_spring.domain.space.application.port.in.CreateSpaceUseCase;

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

    private final CreateBoardUseCase createBoardUseCase;
    private final UpdateCommentInDiscordUseCase updateCommentInDiscordUseCase;
    //private final CreateSpaceUseCase createSpaceUseCase;
    private final CreateDiscordMessageOnThreadPort createDiscordMessageOnThreadPort;
    private final WebHookPort webHookPort;
    private final EditDiscordMessageAdapter editDiscordMessageAdapter;
    private final DiscordUtil  discordUtil;
    private final UpdateMessageInDiscordPort updateMessageInDiscordPort;
    private final DeleteDiscordWebHookMessagePort deleteDiscordWebHookMessagePort;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        Message msg = event.getMessage();
        if (msg.getAuthor().isBot()) {
            return;
        }

        if (msg.getContentRaw().equals("!ping")) {
            //event.getChannel().asTextChannel().sendMessage().set
            msg.reply("pong").queue();
            return;
        }
        if (msg.getContentRaw().equals("!webHook")) {
            List<Webhook> hookList = event.getChannel().asTextChannel().retrieveWebhooks().complete();
            for (Webhook hook : hookList) {
                System.out.println("\n" + hook.getName() + "\n");
            }



            Webhook webHook = event.getChannel().asTextChannel().createWebhook("generate WebHook").complete();
            webHook.sendMessage("only webhook Message").setAvatarUrl(event.getAuthor().getAvatarUrl()).setUsername(event.getMember().getEffectiveName()).queue();
            //event.getChannel().asTextChannel().retrieveWebhooks()

            //WebhookClient.createClient().get
            WebhookClient.createClient(event.getJDA(), webHook.getUrl()).sendMessage("webHook message")
                    .setAvatarUrl(event.getAuthor().getAvatarUrl()).setUsername(event.getMember().getEffectiveName()).queue();
            return;
        }
        if (msg.getContentRaw().equals("!pingping")) {
            CreateDiscordWebHookMessageCommand command = CreateDiscordWebHookMessageCommand.builder()
                    .channelDiscordId(event.getChannel().getIdLong())
                    .guildDiscordId(event.getGuild().getIdLong())
                    .webHookUrl("https://discordapp.com/api/webhooks/1327500273425584250/_U2jXVFxmSq2XiFemb-nOn44PDMZs3KiX8ZXd2cF6mjRMO2D6QuN5qkLyxN37A3qSAr2")
                    .content("spring server message test success")
                    .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                    .name(event.getMember().getEffectiveName())
                    .build();
            createDiscordWebHookMessagePort.send(command).thenAccept(result -> {
                try {
                    event.getMessage().reply("success (id = " + result + ")").queue();
                } catch (Exception e) {
                    event.getMessage().reply("error : " + e.toString()).queue();
                }
            });

        }
            if (msg.getContentRaw().startsWith("!member:")) {
                System.out.println("\n\n\nmembertext\n\n\n");
                System.out.println("mem:" + msg.getGuild().getMembers().size());
                Long spaceId = Long.parseLong(msg.getContentRaw().split(":")[1]);
                loadSpaceMemberPort.loadSpaceMemberBySpaceId(spaceId).stream().forEach(spaceMember -> {
                    msg.getChannel().sendMessage(
                            "\n" + spaceMember.getNickname() + ":" + spaceMember.getId() + ":Manager:" + spaceMember.isManager()).queue();

                });
                return;
            }


                if (msg.getContentRaw().equals("!threadping")) {

                    CreateDiscordThreadCommand command = CreateDiscordThreadCommand.builder()
                            .channelDiscordId(event.getChannel().getIdLong())
                            .guildDiscordId(event.getGuild().getIdLong())
                            .webHookUrl(webHookPort.getOrCreate(event.getChannel().getIdLong()))
                            .contentMessage("spring server thread test success")
                            .threadName("test thread name 12")
                            .startMessage("start message 12")
                            .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                            .userName(event.getMember().getEffectiveName())

                            .build();
                    createDiscordThreadPort.create(command).thenAccept(result -> {
                        try {
                            event.getMessage().reply("success (id = " + result + ")").queue();
                        } catch (Exception e) {
                            event.getMessage().reply("error : " + e.toString()).queue();
                        }
                    }).exceptionally(t -> {
                        System.out.println("\n\nerror : " + t.toString());
                        return null;
                    });
                    return;
                }

                if (msg.getContentRaw().startsWith("!forumping:")) {
                    Long channelId = Long.parseLong(msg.getContentRaw().split(":")[1]);
                    if(!discordUtil.isForumChannel(channelId)){
                        event.getMessage().reply("is not forum channel").queue();
                        System.out.println("is not forum channel");
                        return ;
                    }
                    CreateDiscordThreadCommand command = CreateDiscordThreadCommand.builder()
                            .channelDiscordId(channelId)
                            .guildDiscordId(event.getGuild().getIdLong())
                            .webHookUrl(webHookPort.getOrCreate(event.getGuild().getForumChannelById(channelId).getIdLong()))
                            .contentMessage("spring server message test success\nhi\ncontent")
                            .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                            .userName(event.getMember().getEffectiveName())
                            .threadName("thread name yee")
                            .build();
                    createDiscordThreadPort.create(command).thenAccept(result -> {
                        try {
                            event.getMessage().reply("success (id = " + result + ")" + "\nchannel ID : " + event.getChannel().getId()).queue();
                        } catch (Exception e) {
                            event.getMessage().reply("error : " + e.toString()).queue();
                        }
                    }).exceptionally(t -> {
                        System.out.println("\n\nerror : " + t.toString());
                        return null;
                    });

                    return;

                }
                if(msg.getContentRaw().equals("!getWebHook")){
//                    Webhook webHook = msg.getChannel().asTextChannel().retrieveWebhooks().complete().stream().filter(webhook->{
//                                return webhook.getName().equals("Space_WebHook");
//                            })
//                            //.map(webhook -> webhook.getUrl())
//                            .findFirst().get();
                    msg.reply(webHookPort.getOrCreate(msg.getChannelIdLong())).queue();

                }


                if (msg.getContentRaw().equals("!send")) {
                    CreateDiscordWebHookMessageCommand command = CreateDiscordWebHookMessageCommand.builder()
                            .title("message")
                            .content("content")
                            .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                            .webHookUrl(webHookPort.getOrCreate(discordUtil.getRootChannelId(event.getChannel())  ))                          .guildDiscordId(event.getGuild().getIdLong())
                            .channelDiscordId(event.getChannel().getIdLong())
                            .discordTags(List.of())
                            .name(event.getMember().getEffectiveName())
                            .attachmentsUrl(List.of("https://project-space-image-storage.s3.ap-northeast-2.amazonaws.com/test-image/2024년+『ICT멘토링』+프로젝트+모집+공고문.pdf", "https://cdn.discordapp.com/attachments/1325780875614621801/1347884828254666823/DALLE_2025-02-20_16.47.07_-_A_cute_cartoon_frog_with_a_large_wide-open_black_mouth_where_a_bright_green_letter_K_is_inside_standing_out_against_the_dark_background_of_its_mo.webp?ex=67cd7311&is=67cc2191&hm=b6f7de1b787333fbf9386e1d2f1751ef2fe601d14c28f66f7c8ebcfc43e24910&"))
                            .build();
                    createDiscordWebHookMessagePort.send(command).thenAccept(result -> {
                        try {
                            event.getMessage().reply("success \n(id = " + result + ")" + "\nchannel ID : " + event.getChannel().getId()).queue();
                        } catch (Exception e) {
                            event.getMessage().reply("error : " + e.toString()).queue();
                        }
                    }).exceptionally(t -> {
                        System.out.println("\n\nerror : " + t.toString());
                        return null;
                    });
                    return;
                }



        if(msg.getContentRaw().startsWith("!editThreadName")){
            event.getChannel().asThreadChannel().getManager().setName("바보").queue();

            return;
        }

        if(msg.getContentRaw().startsWith("!pay")){

            return;
        }



        if(msg.getContentRaw().startsWith("!edit:")){
            Long parentChannelId= event.getChannel().asThreadChannel().getParentChannel().getIdLong();
            Long msgId = Long.parseLong(msg.getContentRaw().split(":")[1]);
            editDiscordMessageAdapter.editMessage(
                    webHookPort.getOrCreate(parentChannelId)
                    ,parentChannelId
                    ,msgId
                    ,"임시"
                    ,"임시 메세지"
                    ,List.of()
                    );
        }
        if(msg.getContentRaw().startsWith("!delete:")) {
            String[] commands = msg.getContentRaw().split(":");
            Long msgId = Long.parseLong(commands[1]);
            Long originChannelId=discordUtil.getRootChannelId(event.getChannel());
            deleteDiscordWebHookMessagePort.delete(webHookPort.getOrCreate(originChannelId),
                    event.getGuild().getIdLong()
            ,event.getChannel().getIdLong(),msgId);

        }
        if(msg.getContentRaw().startsWith("!deleteforum:")) {
            String[] commands = msg.getContentRaw().split(":");
            Long msgId = Long.parseLong(commands[1]);
            Long originChannelId=discordUtil.getRootChannelId(event.getChannel());
            deleteDiscordWebHookMessagePort.deleteThread(webHookPort.getOrCreate(originChannelId),
                    event.getGuild().getIdLong()
                    ,event.getChannel().getIdLong(),msgId);

        }
        if(msg.getContentRaw().startsWith("!editcomment:")) {
            String[] commands = msg.getContentRaw().split(":");
            Long msgId = Long.parseLong(commands[1]);
            Long originChannelId=discordUtil.getRootChannelId(event.getChannel());
            event.getChannel().getLatestMessageIdLong();
            System.out.println("last message:"+event.getChannel().getLatestMessageIdLong());
//            updateMessageInDiscordPort.editThreadMessage(webHookPort.getOrCreate(originChannelId),event.getChannel().getIdLong()
//            ,msgId,"new content by port");
            updateCommentInDiscordUseCase.updateCommentInDiscord(UpdateCommentInDiscordCommand.builder()
                    .discordIdOfBoard(originChannelId)
                    .discordIdOfPost(event.getChannel().getIdLong())
                    .discordIdOfComment(msgId)
                    .webHookUrl(webHookPort.getOrCreate(originChannelId))
                    .newContent("edited new comment")
                    .build());

        }
        if(msg.getContentRaw().startsWith("!channelinfo")) {
            event.getMessage().reply(event.getChannel().getHistoryBefore(event.getChannel().getLatestMessageIdLong(),10).complete()
                    .getRetrievedHistory().stream().map(message->message.getContentRaw()+"/"+message.getId()).toList().toString()).queue();
        }
        if(msg.getContentRaw().startsWith("!sendcomment:")){
            Long originChannelId=discordUtil.getRootChannelId(event.getChannel());
            CreateDiscordMessageOnThreadCommand command = CreateDiscordMessageOnThreadCommand.builder()
                    .originPostTitle("origin title")
                    .originChannelId(originChannelId)
                    .originPostId(1L)
                    .content("comment content")
                    .webHookUrl(webHookPort.getOrCreate(originChannelId))
                    .guildDiscordId(event.getGuild().getIdLong())
                    .userName(event.getMember().getEffectiveName())
                    .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                    .threadChannelDiscordId(event.getChannel().getIdLong())
                    .build();
            try {
                createDiscordMessageOnThreadPort.sendToThread(command).thenAccept(msgId -> {
                    System.out.println("commentId:" + msgId);
                    try {
                        club.minnced.discord.webhook.WebhookClient.withUrl(webHookPort.getOrCreate(originChannelId)).onThread(event.getChannel().getIdLong())
                                .edit(msgId, "수정된 댓글").get();
                    }catch(Exception e){
                        System.out.println("error1 : "+e.getMessage());
                    }
                });
            }catch(Exception e){
                System.out.println("error2 : "+e.getMessage());
            }
        }



                if (msg.getContentRaw().startsWith("!comment:")) {
                    String[] commands = msg.getContentRaw().split(":");
                    Long msgId = Long.parseLong(commands[1]);
                    Long channelId = Long.parseLong(commands[2]);

                    CreateDiscordMessageOnThreadCommand command = CreateDiscordMessageOnThreadCommand.builder()
                            .originPostId(0L)
                            .originChannelId(channelId)
                            .avatarUrl(event.getMember().getEffectiveAvatarUrl())
                            .webHookUrl(
                                    webHookPort.getOrCreate(event.getChannel().getIdLong()))
                            .userName(event.getMember().getEffectiveName())
                            .threadChannelDiscordId(msgId)
                            .content("reply success")
                            .guildDiscordId(event.getGuild().getIdLong())
                            .originPostTitle("임시-title")
                            .build();
                    createDiscordMessageOnThreadPort.sendToThread(command).thenAccept(result -> {
                        try {
                            event.getMessage().reply("success \n(id = " + result + ")" + "\nchannel ID : " + event.getChannel().getId()).queue();
                        } catch (Exception e) {
                            event.getMessage().reply("error : " + e.toString()).queue();
                        }
                    }).exceptionally(t -> {
                        System.out.println("\n\nerror : " + t.toString());
                        return null;
                    });

                }

                if (msg.getContentRaw().startsWith("!member:")) {
                    System.out.println("\n\n\nmembertext\n\n\n");
                    System.out.println("mem:" + msg.getGuild().getMembers().size());
                    Long spaceId = Long.parseLong(msg.getContentRaw().split(":")[1]);
                    loadSpaceMemberPort.loadSpaceMemberBySpaceId(spaceId).stream().forEach(spaceMember -> {
                        msg.getChannel().sendMessage("\n" + spaceMember.getNickname() + ":" + spaceMember.getId()).queue();
                    });
                    return;
                }

                if (msg.getContentRaw().equals("!mention")) {


                    event.getChannel().sendMessage("hi" + event.getMember().getAsMention()).queue();
                }
                if (msg.getContentRaw().equals("!addTag")) {

                    if (event.getChannelType().isThread()) {
                        List<ForumTag> tags = event.getChannel().asThreadChannel().getParentChannel().asForumChannel().getAvailableTags();
                        event.getChannel().asThreadChannel().getManager().setAppliedTags(tags).queue();
                    }
                }

                    if (msg.getContentRaw().equals("!createChannel")) {
                        MessageChannelUnion channel = event.getChannel();
                        CreateBoardCommand command = CreateBoardCommand.builder()
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