package space.space_spring.domain.pay.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetRequestPayViewResponse {

    private List<PayRequestInfoDto> payRequestInfoDtoListInComplete = new ArrayList<>();

    private List<PayRequestInfoDto> payRequestInfoDtoListComplete = new ArrayList<>();
}
