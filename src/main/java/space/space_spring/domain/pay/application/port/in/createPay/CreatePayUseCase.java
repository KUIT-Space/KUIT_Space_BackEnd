package space.space_spring.domain.pay.application.port.in.createPay;

public interface CreatePayUseCase {

    Long createPay(CreatePayCommand command);
}
