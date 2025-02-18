package space.space_spring.domain.pay.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class Money {

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 금액은 음수일 수 없습니다. 현재 금액 : " + amount);
        }
        this.amount = amount;
    }

    public static Money of(int amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money add(Money money) {
        return new Money(amount.add(money.getAmount()));
    }

    public Money dividedBy(int number) {
        if (number == 0) {
            throw new IllegalArgumentException("[ERROR] 0으로 나눌 수 없습니다.");
        }

        // 소수점 이하 버림 연산
        BigDecimal result = amount.divide(
                BigDecimal.valueOf(number),
                0,               // 소수점 자리를 0으로 설정
                RoundingMode.DOWN // DOWN 모드 적용 -> 버림(내림)
        );

        return new Money(result);
    }

    public int getAmountInInteger() {
        return amount.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }
}
