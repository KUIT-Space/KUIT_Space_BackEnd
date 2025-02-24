package space.space_spring.domain.discord.adapter.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordRolePort;

@Component
@RequiredArgsConstructor
public class DiscordRoleAdapter implements CreateDiscordRolePort {
    private final JDA jda;
    @Override
    public boolean createRole(Long guildId,String name,Integer rgb){
        jda.getGuildById(guildId).createRole().setName(name).setColor(rgb).complete();
        return true;
    }
}
