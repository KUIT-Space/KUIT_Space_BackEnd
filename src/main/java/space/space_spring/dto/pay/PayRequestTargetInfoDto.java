package space.space_spring.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayRequestTargetInfoDto {

    private Long targetUserId;

    private int requestAmount;
}
