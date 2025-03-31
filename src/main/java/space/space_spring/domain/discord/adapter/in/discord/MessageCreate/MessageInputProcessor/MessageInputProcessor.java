package space.space_spring.domain.discord.adapter.in.discord.MessageCreate.MessageInputProcessor;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MessageInputProcessor {
    boolean supports(MessageReceivedEvent event);
    void process(MessageReceivedEvent event,Long boardId);
}
