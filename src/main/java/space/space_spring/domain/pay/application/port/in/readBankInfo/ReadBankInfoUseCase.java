package space.space_spring.domain.pay.application.port.in.readBankInfo;

import space.space_spring.domain.pay.domain.Bank;

import java.util.Set;

public interface ReadBankInfoUseCase {

    Set<Bank> readBankInfo(Long spaceMemberId);
}
