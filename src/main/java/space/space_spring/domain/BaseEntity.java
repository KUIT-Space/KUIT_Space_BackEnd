package space.space_spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    // 모든 엔티티가 공통으로 가져야할 속성
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String status;
}
