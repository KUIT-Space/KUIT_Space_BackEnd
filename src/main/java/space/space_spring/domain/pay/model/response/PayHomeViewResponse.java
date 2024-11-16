package space.space_spring.domain.pay.model.response;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;

import java.util.List;

@Getter
@Builder
public class PayHomeViewResponse {

    private List<PayRequestInfoDto> payRequestInfoDtos;

    private List<PayTargetInfoDto> payTargetInfoDtos;
}
