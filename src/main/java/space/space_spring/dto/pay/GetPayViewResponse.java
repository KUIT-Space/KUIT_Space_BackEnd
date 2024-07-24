package space.space_spring.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetPayViewResponse {

    private List<PayRequestInfoDto> payRequestInfoDtoList = new ArrayList<>();

    private List<PayReceiveInfoDto> payReceiveInfoDtoList = new ArrayList<>();
}
