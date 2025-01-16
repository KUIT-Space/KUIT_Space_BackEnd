package space.space_spring.global.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    // 모든 엔티티가 공통으로 가져야할 속성

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_date")
    private LocalDateTime lastModifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BaseStatusType status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastModifiedAt = LocalDateTime.now();
        status = BaseStatusType.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }

    protected void initializeBaseEntityFields() {
        onCreate();
    }

    public void updateActive() { this.status = BaseStatusType.ACTIVE; }


    public void updateInactive() { this.status = BaseStatusType.INACTIVE; }
}
