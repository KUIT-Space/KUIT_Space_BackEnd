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
import space.space_spring.global.exception.CustomException;

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
            try {


                event.deferReply(true).queue(msg->{

                });

                Long spaceId = createSpaceUseCase.createSpace(command);

                event.getHook().editOriginal("space setting success!\nspace ID : " + spaceId + "\n")

                        //.setEphemeral(true)
                        .queue();


            }catch(CustomException e) {
                /*
                 * 이미 guild가 space로 등록이 된 경우
                 * */
                event.deferReply().queue();
                event.getHook().editOriginal("this guild already initiated")
                        .queue();
                return;
            }

            return;
        }
    }
}
