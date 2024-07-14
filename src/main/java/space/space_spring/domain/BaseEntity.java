package space.space_spring.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    // 모든 엔티티가 공통으로 가져야할 속성

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_date")
    private LocalDateTime lastModifiedAt;

    @Column(name = "status")
    private String status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastModifiedAt = LocalDateTime.now();
        status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }

    protected void initializeBaseEntityFields() {
        onCreate();
    }
}
