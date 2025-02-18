package space.space_spring.domain.discord.application.port.out.createPay;

public interface CreatePayMessagePort {

    Long createPayMessage(CreatePayMessageCommand command);
}
