package space.space_spring.domain.pay.adapter.in.web.deletePay;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.deletePay.DeletePayUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
public class DeletePayController {

    private final DeletePayUseCase deletePayUseCase;

    @DeleteMapping("/space/{spaceId}/pay/{payRequestId}")
    public BaseResponse<SuccessResponse> deletePay(@JwtLoginAuth Long spaceMemberId, @PathVariable("payRequestId") Long payRequestId) {
        deletePayUseCase.deletePay(spaceMemberId, payRequestId);
        return new BaseResponse<>(new SuccessResponse(true));
    }
}
