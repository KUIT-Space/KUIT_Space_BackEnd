package space.space_spring.domain.pay.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetReceivePayViewResponse {

    private List<PayTargetInfoDto> payReceiveInfoDtoListIncomplete = new ArrayList<>();

    private List<PayTargetInfoDto> payReceiveInfoDtoListComplete = new ArrayList<>();
}
