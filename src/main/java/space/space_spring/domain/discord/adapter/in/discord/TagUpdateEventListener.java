package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateAppliedTagsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class TagUpdateEventListener extends ListenerAdapter {
    private final UpdatePostUseCase updatePostUseCase;
    private final LoadBoardCachePort loadBoardCachePort;
    @Override
    public void onChannelUpdateAppliedTags(ChannelUpdateAppliedTagsEvent event){
        Long parentChannelId = event.getChannel().asThreadChannel().getParentChannel().getIdLong();
        //log.info("parentChannelId:"+parentChannelId);

        Optional<Long> boardId = loadBoardCachePort.findByDiscordId(parentChannelId);

        if(boardId.isEmpty()){
            //log.info("not in cache. ignore");
            return;
        }
        Long threadId = event.getChannel().getIdLong();
        List<Long> newTags = event.getNewTags().stream().map(tag->tag.getIdLong()).toList();
        updatePostUseCase.updateTags(threadId,newTags);
    }
}
