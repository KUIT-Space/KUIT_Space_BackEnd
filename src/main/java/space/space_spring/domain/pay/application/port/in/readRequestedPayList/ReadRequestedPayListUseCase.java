package space.space_spring.domain.pay.application.port.in.readRequestedPayList;

public interface ReadRequestedPayListUseCase {

    ResultOfReadRequestedPayList readRequestedPayList(Long spaceMemberId);
}
