package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.pay.application.port.in.validatePayTarget.ValidatePayTargetUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;

@Service
@RequiredArgsConstructor
public class ValidatePatTargetService implements ValidatePayTargetUseCase {
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;
    public boolean hasPayTarget(Long spaceMember, Long payRequestId){
        return loadPayRequestTargetPort.loadByPayRequestId(payRequestId).stream()
                .anyMatch(payRequestTarget -> payRequestTarget.getTargetMemberId().equals(spaceMember));
    }

}
