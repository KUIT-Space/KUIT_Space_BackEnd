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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;
        Bank bank = (Bank) o;
        return bank.getName().equals(name) && bank.getAccountNumber().equals(accountNumber);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, accountNumber);
    }
}
