package space.space_spring.domain.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;

@Getter
public class Content {

    private static final int MAX_LENGTH = 2000;
    private static final int MIN_LENGTH = 20;

    @Lob
    @Column(nullable = false)
    private String value;

    private Content(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Content는 비어있을 수 없습니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Content의 길이는 최대 " + MAX_LENGTH + "자까지 가능합니다.");
        }
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Content의 길이는 최소 " + MIN_LENGTH + "자이어야 합니다.");
        }
        this.value = value;
    }

    public static Content of(String value) {
        return new Content(value);
    }
}
