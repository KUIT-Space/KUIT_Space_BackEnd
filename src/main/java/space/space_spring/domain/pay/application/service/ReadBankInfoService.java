package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.pay.application.port.in.readBankInfo.ReadBankInfoUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.PayRequest;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadBankInfoService implements ReadBankInfoUseCase {

    private final LoadPayRequestPort loadPayRequestPort;

    /**
     * 일단 개수제한, 정렬 없이 전부 반환
     */
    @Override
    public Set<Bank> readBankInfo(Long spaceMemberId) {
        return loadPayRequestPort.loadByPayCreatorId(spaceMemberId)
                .stream()
                .map(PayRequest::getBank)
                .collect(Collectors.toUnmodifiableSet());       // 중복 제거
    }
}
