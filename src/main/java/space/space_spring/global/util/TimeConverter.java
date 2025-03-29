package space.space_spring.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeConverter {

    public static LocalDateTime convertUtcToKst(LocalDateTime utcDateTime) {
        return utcDateTime.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }

}
