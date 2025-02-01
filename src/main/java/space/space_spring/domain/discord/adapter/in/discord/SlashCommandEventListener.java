package space.space_spring.domain.discord.adapter.in.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SlashCommandEventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        switch (event.getName()) {
            case "guildinfo":
                Guild guild = event.getGuild();
                String Guild_info = "Guild-info" +
                        "\nGuild-ID : " + guild.getId() +
                        "\nChannel : " + guild.getChannels().size() +
                        "\nmembers : " + guild.getMembers().size() +
                        "\nthis Channel ID(temp) : " + event.getChannel().getId().toString();
                String channelList = " \nchannels";
                List<GuildChannel> channels = guild.getChannels();
                for (GuildChannel guildChannel : channels) {
                    channelList = channelList + "\n" + guildChannel.getName() + " : " + guildChannel.getId().toString();
                }
                Guild_info = Guild_info + channelList;
                reply(event, Guild_info);
                //guild.loadMembers(member -> {say(event,member.getEffectiveName()+"\n");});

                break;
            case "Member-info":
                String MemberInfo = "Guild Member Info";
                event.getGuild().loadMembers(member -> MemberInfo.concat("\n" + member.getEffectiveName() + " : " + member.getId()));
                reply(event, MemberInfo);
            case "channel-info":
                MessageChannelUnion channel = event.getChannel();
                String channelInfo = "Channel information\n" +
                        channel.getName() + " : " +
                        channel.getId().toString();
                reply(event, channelInfo);

                break;
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();

        }


    }
    private void reply(SlashCommandInteractionEvent event, String content){
        event.reply(content).queue();
    }
}

