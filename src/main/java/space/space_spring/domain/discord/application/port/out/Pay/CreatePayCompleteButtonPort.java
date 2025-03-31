package space.space_spring.domain.discord.application.port.out.Pay;

public interface CreatePayCompleteButtonPort {
    void sendCompleteButton(Long threadId);
}
