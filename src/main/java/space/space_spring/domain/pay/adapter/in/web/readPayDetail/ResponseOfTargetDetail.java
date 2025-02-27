package space.space_spring.domain.pay.adapter.in.web.readPayDetail;

import lombok.Builder;
import space.space_spring.domain.pay.application.port.in.readPayDetail.InfoOfTargetDetail;

public class ResponseOfTargetDetail {

    private Long targetMemberId;

    private String targetMemberName;

    private String targetMemberProfileImageUrl;

    private int requestedAmount;

    private boolean isComplete;

    @Builder
    private ResponseOfTargetDetail(Long targetMemberId, String targetMemberName, String targetMemberProfileImageUrl, int requestedAmount, boolean isComplete) {
        this.targetMemberId = targetMemberId;
        this.targetMemberName = targetMemberName;
        this.targetMemberProfileImageUrl = targetMemberProfileImageUrl;
        this.requestedAmount = requestedAmount;
        this.isComplete = isComplete;
    }

    public static ResponseOfTargetDetail of(InfoOfTargetDetail info) {
        return ResponseOfTargetDetail.builder()
                .targetMemberId(info.getTargetMemberId())
                .targetMemberName(info.getTargetMemberName())
                .targetMemberProfileImageUrl(info.getTargetMemberProfileImageUrl())
                .requestedAmount(info.getRequestedAmount().getAmountInInteger())
                .isComplete(info.isComplete())
                .build();
    }
}
