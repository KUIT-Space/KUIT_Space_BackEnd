package space.space_spring.global.util;

import lombok.Getter;

@Getter
public class NaturalNumber {

    private int number;

    private NaturalNumber(int number) {
        this.number = number;
    }

    public static NaturalNumber of(int number) {
        return new NaturalNumber(number);
    }
}
