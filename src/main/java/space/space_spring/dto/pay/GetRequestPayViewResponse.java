package space.space_spring.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetRequestPayViewResponse {

    private List<PayRequestInfoDto> payRequestInfoDtoListInComplete = new ArrayList<>();

    private List<PayRequestInfoDto> payRequestInfoDtoListComplete = new ArrayList<>();
}
