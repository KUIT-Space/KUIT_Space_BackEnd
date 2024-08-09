package space.space_spring.dto.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayTargetInfoDto {

    private Long targetUserId;

    private String targetUserName;

    private String targetUserProfileImg;

    private int requestAmount;

    private boolean isComplete;
}
