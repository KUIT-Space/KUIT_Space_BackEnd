package space.space_spring.global.config.JDA;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.domain.BoardType;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Component

public class SlashCommandRegistrar {
    private final JDA jda;

    @Autowired
    public SlashCommandRegistrar(JDA jda) {
        this.jda = jda;
        this.registerCommands();
    }
    private void registerCommands(){
        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("init-space","initiate this Guild to Space. first call this command when add Space Bot")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.addCommands(
                Commands.slash("guildinfo","print Guild information.")
                        .addOption(STRING,"test-option","test-option",false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                //.setGuildOnly(true) // this doesn't make sense in DMs
                //.setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );
        commands.addCommands(
                Commands.slash("member-info","Get Guild Members info")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );

        commands.addCommands(
                Commands.slash("channel-info","get this Channel info")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );

        commands.addCommands(
                Commands.slash("set-board","get this Channel info")

//                        .addOptions(
//                                new OptionData(OptionType.STRING, "webhook-url", "이 채널에 WebHook URL 주소를 입력해주세요")
//                                        .setDescription("채널 설정 -> Integrations -> WebHooks -> Copy WebHook URL")
//                                        .setRequired(false)
//                        )
//                        .addOptions(
//                                new OptionData(OptionType.STRING, "board-type", "게시판 종류를 선택해주세요")
//
//                                .addChoice("post",BoardType.POST.name())
//                                .addChoice("question",BoardType.QUESTION.name())
//                                .setRequired(true)
//
//                        )
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))

        );
        commands.addCommands(
                Commands.slash("move-message","move message to other channel")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );

        commands.queue();
    }
}
