package space.space_spring.domain.pay.adapter.in.web.completePay;

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
public class CompletePayController {

    private final CompletePayUseCase completePayUseCase;

    @PatchMapping("/space/{spaceId}/pay/{payRequestTargetId}/complete")
    public BaseResponse<SuccessResponse> completeForRequestedPay(@JwtLoginAuth Long spaceMemberId, @PathVariable("spaceId") Long spaceId, @PathVariable("payRequestTargetId") Long payRequestTargetId) {
        /**
         * 토큰 수정하면 토큰 spaceId == url spaceId 확인하는 validation 추가
         */

        completePayUseCase.completeForRequestedPay(spaceMemberId, payRequestTargetId);
        return new BaseResponse<>(new SuccessResponse(true));
    }
}
