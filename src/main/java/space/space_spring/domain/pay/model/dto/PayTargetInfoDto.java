package space.space_spring.domain.pay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayTargetInfoDto {

    private Long targetUserId;

    private String targetUserName;

    private String targetUserProfileImg;

    private int requestedAmount;

    private boolean isComplete;
}
