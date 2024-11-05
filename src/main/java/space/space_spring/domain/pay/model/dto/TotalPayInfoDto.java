package space.space_spring.domain.pay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class TotalPayInfoDto {

    private Long payRequestId;

    private String bankName;

    private String bankAccountNum;

    private int totalAmount;

    private int receiveAmount;

    private int totalTargetNum;

    private int receiveTargetNum;

    private List<PayTargetInfoDto> payTargetInfoDtoList = new ArrayList<>();

    private boolean isComplete;

    private LocalDateTime createdAt;           // 정산 생성일 정보

}
