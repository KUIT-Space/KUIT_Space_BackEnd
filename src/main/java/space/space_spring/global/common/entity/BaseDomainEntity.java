package space.space_spring.global.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.time.LocalDateTime;

@Getter
public class BaseDomainEntity {

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private BaseStatusType status;

    protected BaseDomainEntity(LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType status) {
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.status = status;
    }

    public static BaseDomainEntity create(LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType status) {
        return new BaseDomainEntity(createdAt, lastModifiedAt, status);
    }

}
