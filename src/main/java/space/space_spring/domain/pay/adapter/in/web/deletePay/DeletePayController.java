package space.space_spring.domain.pay.adapter.in.web.deletePay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pay", description = "정산 관련 API")
public class DeletePayController {

    private final DeletePayUseCase deletePayUseCase;

    @Operation(summary = "정산 요청 삭제", description = """
            
            정산 요청을 생성한 멤버가 해당 정산 요청을 삭제합니다.
            
            """)
    @DeleteMapping("/space/{spaceId}/pay/{payRequestId}")
    public BaseResponse<SuccessResponse> deletePay(@JwtLoginAuth Long spaceMemberId, @PathVariable("payRequestId") Long payRequestId) {
        deletePayUseCase.deletePay(spaceMemberId, payRequestId);
        return new BaseResponse<>(new SuccessResponse(true));
    }
}
