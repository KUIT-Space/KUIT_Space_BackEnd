package space.space_spring.domain.discord.adapter.out;

import org.springframework.stereotype.Repository;
import space.space_spring.domain.discord.application.port.out.CreatePayInDiscordPort;
import space.space_spring.domain.discord.application.port.out.CreatePayMessageCommand;

@Repository
public class DiscordPayAdapter implements CreatePayInDiscordPort {
    @Override
    public Long createPayMessage(CreatePayMessageCommand command) {
        return 0L;          // 상준씨 구현해주세여
    }
}
