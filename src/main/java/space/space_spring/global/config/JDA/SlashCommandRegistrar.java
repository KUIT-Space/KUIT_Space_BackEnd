package space.space_spring.global.config.JDA;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.stereotype.Component;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Component
@RequiredArgsConstructor
public class SlashCommandRegistrar {
    private final JDA jda;

    private void registerCommand(){
        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("init-space","initiate this Guild to Space. first call this command when add Space Bot")
        );
        commands.addCommands(
                Commands.slash("guildinfo","print Guild information.")
                        .addOption(STRING,"test-option","test-option",false)
                //.setGuildOnly(true) // this doesn't make sense in DMs
                //.setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );
        commands.addCommands(
                Commands.slash("member-info","Get Guild Members info")
        );

        commands.addCommands(
                Commands.slash("channel-info","get this Channel info")
        );

        commands.queue();
    }
}
