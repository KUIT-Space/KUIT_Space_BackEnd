package space.space_spring.domain.pay.adapter.in.web.readBankInfo;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;

@Getter
public class BankInfo {

    private String bankName;

    private String bankAccountNumber;

    private BankInfo(String bankName, String bankAccountNumber) {
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
    }

    public static BankInfo of(Bank bank) {
        return new BankInfo(bank.getName(), bank.getAccountNumber());
    }
}
