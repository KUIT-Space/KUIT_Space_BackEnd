package space.space_spring.dto.pay.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.dto.pay.dto.RecentPayRequestBankInfoDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetRecentPayRequestBankInfoResponse {

    private List<RecentPayRequestBankInfoDto> recentPayRequestBankInfoDtoList = new ArrayList<>();

}
