package space.space_spring.domain.pay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayTargetInfoDto {

    private Long payRequestTargetId;

    private String payCreatorName;

    private int requestedAmount;

    private String bankName;

    private String bankAccountNum;
}
