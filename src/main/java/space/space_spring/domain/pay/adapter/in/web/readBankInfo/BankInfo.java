package space.space_spring.domain.pay.adapter.in.web.readBankInfo;

import space.space_spring.domain.pay.domain.Bank;

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
