package space.space_spring.domain.discord.application.port.out;

public interface CreatePayMessagePort {

    Long createPayMessage(CreatePayMessageCommand command);
}
