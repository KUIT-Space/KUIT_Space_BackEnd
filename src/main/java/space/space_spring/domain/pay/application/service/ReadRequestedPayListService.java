package space.space_spring.domain.pay.application.service;

import org.springframework.stereotype.Service;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ReadRequestedPayListUseCase;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;

@Service
public class ReadRequestedPayListService implements ReadRequestedPayListUseCase {

    @Override
    public ResultOfReadRequestedPayList readRequestedPayList(Long spaceMemberId) {
        return null;
    }
}
