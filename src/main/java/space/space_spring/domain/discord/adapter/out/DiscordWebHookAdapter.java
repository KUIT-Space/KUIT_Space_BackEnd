package space.space_spring.domain.discord.adapter.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.springframework.stereotype.Component;
import net.dv8tion.jda.api.JDA;
import space.space_spring.domain.discord.application.port.out.WebHookPort;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_CHANNEL_TYPE_WRONG;

@Component
@RequiredArgsConstructor
public class DiscordWebHookAdapter implements WebHookPort {
    private final JDA jda;

    private final String SPACE_WEBHOOK_NAME = "Space_WebHook";

    @Override
    public String createSpaceWebHook(Long channelDiscordId){
        return getChannel(channelDiscordId).createWebhook(SPACE_WEBHOOK_NAME).complete().getUrl();
    }

    @Override
    public Optional<String> getSpaceWebHook(Long channelDiscordId){
        return getChannel(channelDiscordId).retrieveWebhooks().complete().stream().filter(webhook->{
            return webhook.getName().equals(SPACE_WEBHOOK_NAME);
        })
            .map(webhook -> webhook.getUrl())
            .findFirst();
    }

    @Override
    public String getOrCreate(Long channelId){
        Optional<String> webhookUrl = this.getSpaceWebHook(channelId);
        if(webhookUrl.isEmpty()){
            return this.createSpaceWebHook(channelId);
        }
        return webhookUrl.get();
    }

    private IWebhookContainer getChannel(Long channelDiscordId){
        GuildChannel channel = jda.getGuildChannelById(channelDiscordId);
        if(channel.getType().equals(ChannelType.TEXT)){
            return jda.getTextChannelById(channelDiscordId);
        }
        if(channel.getType().equals(ChannelType.FORUM)){
            return jda.getForumChannelById(channelDiscordId);
        }
        throw new CustomException(DISCORD_CHANNEL_TYPE_WRONG,DISCORD_CHANNEL_TYPE_WRONG+"\nwebhook 생성과정에서 채널 타입이 Text와 Forum이 아닙니다");
    }

}
