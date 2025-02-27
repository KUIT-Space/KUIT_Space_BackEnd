package space.space_spring.domain.pay.application.port.in.readPayDetail;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;

@Getter
public class InfoOfTargetDetail {

    private Long targetMemberId;

    private String targetMemberName;

    private String targetMemberProfileImageUrl;

    private Money requestedAmount;

    private boolean isComplete;

    private InfoOfTargetDetail(Long targetMemberId, String targetMemberName, String targetMemberProfileImageUrl, Money requestedAmount, boolean isComplete) {
        this.targetMemberId = targetMemberId;
        this.targetMemberName = targetMemberName;
        this.targetMemberProfileImageUrl = targetMemberProfileImageUrl;
        this.requestedAmount = requestedAmount;
        this.isComplete = isComplete;
    }

    public static InfoOfTargetDetail of(Long targetMemberId, String targetMemberName, String targetMemberProfileImageUrl, Money requestedAmount, boolean isComplete) {
        return new InfoOfTargetDetail(targetMemberId, targetMemberName, targetMemberProfileImageUrl, requestedAmount, isComplete);
    }
}
