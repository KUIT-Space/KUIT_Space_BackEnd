package space.space_spring.global.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDateTime;

import lombok.NoArgsConstructor;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseJpaEntity {

    // 모든 엔티티가 공통으로 가져야할 속성

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_date")
    private LocalDateTime lastModifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BaseStatusType status;

    protected BaseJpaEntity(LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType status) {
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {         // null 인 경우에만 기본값 세팅
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.lastModifiedAt == null) {
            this.lastModifiedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = BaseStatusType.ACTIVE;
        }
    }

    /**
     * DB로 update query 날라갈 시에 lastModifiedAt value update
     */
    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }


    public void updateToActive() { this.status = BaseStatusType.ACTIVE; }

    public void updateToInactive() { this.status = BaseStatusType.INACTIVE; }

    public boolean isActive() {
        return this.status == BaseStatusType.ACTIVE;
    }
}
