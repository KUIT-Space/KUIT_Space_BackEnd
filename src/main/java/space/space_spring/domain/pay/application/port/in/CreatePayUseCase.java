package space.space_spring.domain.pay.application.port.in;

public interface CreatePayUseCase {

    Long createPay(CreatePayCommand command);
}
