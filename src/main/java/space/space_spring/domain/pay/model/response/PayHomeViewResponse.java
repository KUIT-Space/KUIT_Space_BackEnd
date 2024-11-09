package space.space_spring.domain.pay.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PayHomeViewResponse {

    private List<PayRequestInfoDto> payRequestInfoDtos;

    private List<PayTargetInfoDto> payTargetInfoDtos;
}
