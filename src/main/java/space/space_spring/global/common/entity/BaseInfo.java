package space.space_spring.global.common.entity;

import lombok.Getter;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.time.LocalDateTime;

@Getter
public class BaseInfo {

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private BaseStatusType status;

    private BaseInfo(LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType status) {
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.status = status;
    }

    public static BaseInfo of(LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType status) {
        return new BaseInfo(createdAt, lastModifiedAt, status);
    }

    public static BaseInfo ofEmpty() {
        return new BaseInfo(null, null, null);
    }
}
