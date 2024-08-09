package space.space_spring.dto.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayReceiveInfoDto {

    private Long payRequestTargetId;

    private String payCreatorName;

    private int requestAmount;
}
