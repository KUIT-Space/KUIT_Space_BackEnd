package space.space_spring.global.util;

import lombok.Getter;

import java.util.Objects;

@Getter
public class NaturalNumber {

    private int number;

    private NaturalNumber(int number) {
        this.number = number;
    }

    public static NaturalNumber of(int number) {
        return new NaturalNumber(number);
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
