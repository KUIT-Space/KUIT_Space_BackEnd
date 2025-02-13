package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.Bank;

public interface LoadPayRequestInfoPort {

    Bank loadBankOfPayRequestById(Long payRequestId);
}
