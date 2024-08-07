package space.space_spring.dto.pay.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.dto.pay.dto.PayReceiveInfoDto;
import space.space_spring.dto.pay.dto.PayRequestInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetPayViewResponse {

    private List<PayRequestInfoDto> payRequestInfoDtoList = new ArrayList<>();

    private List<PayReceiveInfoDto> payReceiveInfoDtoList = new ArrayList<>();
}
