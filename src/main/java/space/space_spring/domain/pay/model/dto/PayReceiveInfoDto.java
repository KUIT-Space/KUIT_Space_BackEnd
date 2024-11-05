package space.space_spring.domain.pay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayReceiveInfoDto {

    private Long payRequestTargetId;

    private String payCreatorName;

    private int requestAmount;

    private String bankName;

    private String bankAccountNum;
}
