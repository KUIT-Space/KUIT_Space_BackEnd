package space.space_spring.domain.pay.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.RecentPayRequestBankInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetRecentPayRequestBankInfoResponse {

    private List<RecentPayRequestBankInfoDto> recentPayRequestBankInfoDtoList = new ArrayList<>();

}
