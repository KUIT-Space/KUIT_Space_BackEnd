package space.space_spring.domain.pay.adapter.out.persistence.jpaEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Pay_Request_Target")
@Getter
public class PayRequestTargetJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_request_target_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity targetMember;           // 정산 대상 멤버

    @ManyToOne
    @JoinColumn(name = "pay_request_id")
    @NotNull
    private PayRequestJpaEntity payRequest;

    @Column(name = "requested_amount")
    @NotNull
    private int requestedAmount;

    @Column(name = "is_complete")
    @NotNull
    private boolean isComplete;

    @Builder
    private PayRequestTargetJpaEntity(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequest, int requestedAmount, boolean isComplete,
                                      LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        super(createdAt, lastModifiedAt, baseStatus);

        this.targetMember = targetMember;
        this.payRequest = payRequest;
        this.requestedAmount = requestedAmount;
        this.isComplete = isComplete;
    }

    public static PayRequestTargetJpaEntity create(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequest, int requestedAmount,
                                                   LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        return PayRequestTargetJpaEntity.builder()
                .targetMember(targetMember)
                .payRequest(payRequest)
                .requestedAmount(requestedAmount)
                .isComplete(false)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .baseStatus(baseStatus)
                .build();
    }

    public static PayRequestTargetJpaEntity of(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequest, int requestedAmount, boolean isComplete,
                                               LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        return PayRequestTargetJpaEntity.builder()
                .targetMember(targetMember)
                .payRequest(payRequest)
                .requestedAmount(requestedAmount)
                .isComplete(isComplete)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .baseStatus(baseStatus)
                .build();
    }

    public void changeCompletionStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
