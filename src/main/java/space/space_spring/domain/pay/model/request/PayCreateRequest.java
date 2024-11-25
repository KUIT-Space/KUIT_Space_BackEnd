package space.space_spring.domain.pay.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PayCreateRequest {

    /**
     * PayRequest 엔티티 생성 시 필요한 정보
     */
    @NotBlank(message = "총 정산 요청 금액은 공백일 수 없습니다.")
    private int totalAmount;

    @NotBlank(message = "은행 이름은 공백일 수 없습니다.")
    private String bankName;

    @NotBlank(message = "은행 계좌 번호는 공백일 수 없습니다.")
    private String bankAccountNum;

    /**
     * PayRequestTarget 엔티티 생성 시 필요한 정보
     * <targetUserId requestedAmount> 쌍
     */
    private List<TargetInfoRequest> targetInfoRequests;

    @Getter
    @NoArgsConstructor
    private static class TargetInfoRequest {

        @NotBlank(message = "정산 요청 타겟 유저의 id값은 공백일 수 없습니다.")
        private Long targetUserId;

        @NotBlank(message = "정산 요청 금액은 공백일 수 없습니다.")
        private int requestedAmount;

        public PayCreateServiceRequest.TargetInfo toTargetInfo() {
            return PayCreateServiceRequest.TargetInfo.builder()
                    .targetUserId(targetUserId)
                    .requestedAmount(requestedAmount)
                    .build();
        }
    }

    public PayCreateServiceRequest toServiceRequest() {
        return PayCreateServiceRequest.builder()
                .totalAmount(totalAmount)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .targetInfos(targetInfoRequests.stream()
                        .map(TargetInfoRequest::toTargetInfo)
                        .toList())
                .build();
    }
}
