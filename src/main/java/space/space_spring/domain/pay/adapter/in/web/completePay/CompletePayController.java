package space.space_spring.domain.pay.adapter.in.web.completePay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.completePay.CompletePayUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pay", description = "정산 관련 API")
public class CompletePayController {

    private final CompletePayUseCase completePayUseCase;

    @Operation(summary = "정산 완료 처리(= 송금 완료 처리)", description = """
            
            정산 요청의 대상자가 정산 완료 처리(= 송금 완료 처리)를 합니다.
            
            """)
    @PatchMapping("/space/{spaceId}/pay/{payRequestTargetId}/complete")
    public BaseResponse<SuccessResponse> completeForRequestedPay(@JwtLoginAuth Long spaceMemberId, @PathVariable("spaceId") Long spaceId, @PathVariable("payRequestTargetId") Long payRequestTargetId) {
        completePayUseCase.completeForRequestedPay(spaceMemberId, payRequestTargetId);
        return new BaseResponse<>(new SuccessResponse(true));
    }
}
