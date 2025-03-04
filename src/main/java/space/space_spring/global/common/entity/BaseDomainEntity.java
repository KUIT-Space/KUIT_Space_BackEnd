package space.space_spring.global.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class BaseDomainEntity {

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private BaseStatusType status;

    protected BaseDomainEntity(LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType status) {
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.status = status;
    }
}
