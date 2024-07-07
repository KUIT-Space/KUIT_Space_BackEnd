package space.space_spring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleFailTest {
    @Test
    void testFailure() {
        // 이 테스트는 항상 실패합니다.
        assertEquals(1, 0, "1은 0과 같지 않습니다.");
    }
}
