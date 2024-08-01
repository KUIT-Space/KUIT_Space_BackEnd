package space.space_spring.dto.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentPayRequestBankInfoDto {

    private String bankName;

    private String bankAccountNum;
}
