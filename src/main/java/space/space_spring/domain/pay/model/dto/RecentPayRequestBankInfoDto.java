package space.space_spring.domain.pay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentPayRequestBankInfoDto {

    private String bankName;

    private String bankAccountNum;
}
