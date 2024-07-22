package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import space.space_spring.response.BaseResponse;

@Entity
@Getter
public class PayRequestTarget extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_request_target_id")
    private Long payRequestTargetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_request_id")
    private PayRequest payRequest;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "request_amount")
    private int requestAmount;

    @Column(name = "is_complete")
    private boolean isComplete;

}
