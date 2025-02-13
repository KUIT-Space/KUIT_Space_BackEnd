package space.space_spring.global.config.JDA;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class JDAConfig {

    @Value("${discord.bot-token}")
    private String token;  // application.properties에서 토큰 관리

    @Autowired
    private List<ListenerAdapter> eventListeners; // 모든 리스너 자동 주입

    @Bean(name="eventJDA")
    public JDA eventJDA() throws InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(token,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS);

        // 등록된 모든 이벤트 리스너 추가
        if(eventListeners.isEmpty()){
            System.out.println("event listener is empty");
        }
        for (ListenerAdapter listener : eventListeners) {
            builder.addEventListeners(listener);
        }
        builder.setActivity(Activity.playing("개굴 봇입니다(spring version)"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL);

        return builder.build().awaitReady();
    }


}
