package space.space_spring.dto.pay.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.dto.pay.dto.PayRequestInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetRequestPayViewResponse {

    private List<PayRequestInfoDto> payRequestInfoDtoListInComplete = new ArrayList<>();

    private List<PayRequestInfoDto> payRequestInfoDtoListComplete = new ArrayList<>();
}
