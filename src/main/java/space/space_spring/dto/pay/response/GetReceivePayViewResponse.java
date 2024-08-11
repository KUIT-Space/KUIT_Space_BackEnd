package space.space_spring.dto.pay.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.dto.pay.dto.PayReceiveInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetReceivePayViewResponse {

    private List<PayReceiveInfoDto> payReceiveInfoDtoListIncomplete = new ArrayList<>();

    private List<PayReceiveInfoDto> payReceiveInfoDtoListComplete = new ArrayList<>();
}
