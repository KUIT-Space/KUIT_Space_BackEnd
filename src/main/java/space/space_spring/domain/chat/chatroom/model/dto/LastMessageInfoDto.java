package space.space_spring.domain.chat.chatroom.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
public class LastMessageInfoDto {

    private LocalDateTime lastUpdateTime;

    private HashMap<String, String> lastContent;

    @Builder
    private LastMessageInfoDto(LocalDateTime lastUpdateTime, HashMap<String, String> lastContent) {
        this.lastUpdateTime = lastUpdateTime;
        this.lastContent = lastContent;
    }

    public static LastMessageInfoDto of(LocalDateTime lastUpdateTime, HashMap<String, String> lastContent) {
        return LastMessageInfoDto.builder()
                .lastUpdateTime(lastUpdateTime)
                .lastContent(lastContent)
                .build();
    }
}

