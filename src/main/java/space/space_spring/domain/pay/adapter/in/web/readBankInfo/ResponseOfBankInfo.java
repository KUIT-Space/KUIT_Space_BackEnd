package space.space_spring.domain.pay.adapter.in.web.readBankInfo;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;

import java.util.List;
import java.util.Set;

@Getter
public class ResponseOfBankInfo {

    private List<BankInfo> bankInfos;

    private ResponseOfBankInfo(List<BankInfo> bankInfos) {
        this.bankInfos = bankInfos;
    }

    public static ResponseOfBankInfo of(Set<Bank> banks) {
        return new ResponseOfBankInfo(banks.stream()
                .map(BankInfo::of)
                .toList());
    }
}
