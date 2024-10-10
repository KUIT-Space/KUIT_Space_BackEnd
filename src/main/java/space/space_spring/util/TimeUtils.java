package space.space_spring.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeUtils {

    /**
     * UTC 시간을 서울 시간으로 변환
     */
    public LocalDateTime getEncodedTime(LocalDateTime originalTime) {
        return originalTime
            .atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime();
    }
}
