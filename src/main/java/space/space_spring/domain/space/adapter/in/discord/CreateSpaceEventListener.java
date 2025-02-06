package space.space_spring.domain.space.adapter.in.discord;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.space.application.port.in.CreateSpaceCommand;
import space.space_spring.domain.space.application.port.in.CreateSpaceUseCase;

@Component
@RequiredArgsConstructor
public class CreateSpaceEventListener extends ListenerAdapter {
    private final CreateSpaceUseCase createSpaceUseCase;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        if(event.getName().equals("init-space")){
            //이 길드를 space로 등록합니다
            Guild guild = event.getGuild();
            CreateSpaceCommand command = CreateSpaceCommand.builder()
                    .guildId(guild.getIdLong())
                    .guildName(guild.getName())
                    .build();

            Long spaceId = createSpaceUseCase.createSpace(command);
            if(spaceId==null){
                /*
                * 이미 guild가 space로 등록이 된 경우
                * */
                event.getInteraction()
                        .reply("this guild already initiated\nspace ID : "+spaceId+"\n")
                        .setEphemeral(true)
                        .queue();
                return;
            }
            event.getInteraction()
                    .reply("space setting success!\nspace ID : "+spaceId+"\n")
                    .setEphemeral(true)
                    .queue();


            return;
        }
    }
}
