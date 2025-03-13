package space.space_spring.domain.discord.adapter.in.discord;

import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentUseCase;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostCommand;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;
import space.space_spring.domain.post.domain.Content;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class MessageUpdateEventListener extends ListenerAdapter {
    private final DiscordUtil discordUtil;
    private final LoadBoardCachePort loadBoardCachePort;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DiscordMessageMapper discordMessageMapper;
    @Override
    public void onMessageUpdate(MessageUpdateEvent event){
        if(event.getAuthor().isBot()){
            //log.info("bot message. ignore");
            return;
        }

        MessageChannelUnion channel=event.getChannel();

        if(!isAvailableChannelType(channel))
        {
            //log.info("not Available channel type. ignore");
            return;
        }

        Long parentChannelId = discordUtil.getRootChannelId(channel);
        //log.info("parentChannelId:"+parentChannelId);

        Optional<Long> boardId = loadBoardCachePort.findByDiscordId(parentChannelId);

        if(boardId.isEmpty()){
            //log.info("not in cache. ignore");
            return;
        }

        if(isComment(event)){
            //Todo map comment update command
            //Todo update comment UseCase call
            UpdateCommentFromDiscordCommand command = UpdateCommentFromDiscordCommand.builder()
                    .discordMessageId(event.getMessageIdLong())
                    .Content(event.getMessage().getContentRaw())
                    .build();
            updateCommentUseCase.updateCommentFromDiscord(command);
        }

        //Todo map post update command
        //Todo update post UseCase call
        updatePostUseCase.updatePostFromDiscord(mapToUpdatePost(event));






    }

    private boolean isComment(MessageUpdateEvent event){
        return event.isFromThread()&&event.getChannel().getId().equals(event.getMessage().getId());
    }

    private boolean isAvailableChannelType(MessageChannelUnion channel){
        switch (channel.getType()){
            case TEXT :
            case FORUM:
            case GUILD_PUBLIC_THREAD:
                return true;
            default:
                return false;
        }
    }

    private UpdatePostFromDiscordCommand mapToUpdatePost(MessageUpdateEvent event){
        return UpdatePostFromDiscordCommand.builder()
                .tags(getTagIdList(event))
                .title(event.isFromThread() ?
                        event.getChannel().asThreadChannel().getName()
                        :DiscordMessageMapper.getFirstRow(event.getMessage().getContentRaw(),"\n"))
                .attachments(DiscordMessageMapper.getAttachments(event.getMessage()))

                .postDiscordId(event.getMessageIdLong())
                .isAnonymous(false)
                .content(Content.of(
                        event.isFromThread() ?
                        event.getMessage().getContentRaw()
                        :DiscordMessageMapper.removeFirstRow(event.getMessage().getContentRaw(),"\n")
                ))
                .build();
    }

    private List<Long> getTagIdList(MessageUpdateEvent event){
        if(!event.isFromThread()){
            return List.of();
        }
        if(event.getChannel().getId().equals(event.getMessage().getId())){
            return event.getChannel().asThreadChannel().getAppliedTags().stream().map(tag->tag.getIdLong()).toList();
        }
        return List.of();
    }

}
