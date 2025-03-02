package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.in.CreateBoardInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.CreateBoardInDiscordUseCase;

@Component
@RequiredArgsConstructor
public class CreateBoardEventListener extends ListenerAdapter {
    private final CreateBoardInDiscordUseCase createBoardInDiscordUseCase;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        if(event.getName().equals("set-board")) {

            event.deferReply(true).queue();


            //event.getGuild().loadMembers(member -> MemberInfo.concat("\n" + member.getEffectiveName() + " : " + member.getId()));
            String webHookUrl = event.getOption("webhook-url").getAsString();
            String type = event.getOption("board-type").getAsString();
            String name = event.getChannel().getName();
            Long channelDiscordId = event.getChannelIdLong();
            Long guildDiscordId = event.getGuild().getIdLong();
            Long boardId =createBoardInDiscordUseCase.create(
                CreateBoardInDiscordCommand.builder()
                        .boardType(type)
                        .guildDiscordId(guildDiscordId)
                        .channelDiscordId(channelDiscordId)
                        .name(name)
                        .WebHookUrl(webHookUrl)
                        .build()
            );


            event.getHook().editOriginal("board init success : "+boardId).queue();


        }
    }
}
