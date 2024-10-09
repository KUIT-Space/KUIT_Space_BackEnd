package space.space_spring.dto.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Builder
public class LastMessageInfoDto {

    private LocalDateTime lastUpdateTime;

    private HashMap<String, String> lastContent;
}

