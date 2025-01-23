package space.space_spring.domain.pay.domain;

import lombok.Getter;

@Getter
public class Bank {

    private String name;

    private String accountNumber;

    private Bank(String name, String accountNumber) {
        this.name = name;
        this.accountNumber = accountNumber;
    }

    public static Bank of(String name, String accountNumber) {
        return new Bank(name, accountNumber);
    }
}
