package space.space_spring.domain.discord.adapter.out;

import org.springframework.stereotype.Repository;
import space.space_spring.domain.discord.application.port.out.CreatePayMessagePort;
import space.space_spring.domain.discord.application.port.out.CreatePayMessageCommand;

@Repository
public class DiscordPayAdapter implements CreatePayMessagePort {

    @Override
    public Long createPayMessage(CreatePayMessageCommand command) {
        return 0L;          // 상준씨 구현해주세여
    }

}
