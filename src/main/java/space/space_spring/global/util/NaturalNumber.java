package space.space_spring.global.util;

import lombok.Getter;

import java.util.Objects;

@Getter
public class NaturalNumber {

    private final int number;           // NaturalNumber를 불변객체로 만들기 위해 final 키워드 추가

    private NaturalNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("자연수는 0 이상이어야 합니다.");
        }
        this.number = number;
    }

    public static NaturalNumber of(int number) {
        return new NaturalNumber(number);
    }

    public NaturalNumber add(NaturalNumber other) {
        return new NaturalNumber(number + other.number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NaturalNumber)) return false;
        NaturalNumber naturalNumber = (NaturalNumber) o;
        return number == naturalNumber.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
