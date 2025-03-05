package space.space_spring.domain.pay.adapter.in.web.readPayDetail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.readPayDetail.ReadPayDetailUseCase;
import space.space_spring.domain.pay.application.port.in.readPayDetail.ResultOfReadPayDetail;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pay", description = "정산 관련 API")
public class ReadSinglePayController {

    /**
     * 정산 상세 조회
     */

    private final ReadPayDetailUseCase readPayDetailUseCase;

    @Operation(summary = "정산 요청 상세 조회", description = """
            
            정산 요청을 생성한 멤버가 해당 정산 요청의 상세 정보를 조회힙니다.
            
            """)
    @GetMapping("/space/{spaceId}/pay/{payRequestId}")
    public BaseResponse<ResponseOfReadPayDetail> showPayDetail(@JwtLoginAuth Long spaceMemberId, @PathVariable("payRequestId") Long payRequestId) {
        return new BaseResponse<>(ResponseOfReadPayDetail.of(readPayDetailUseCase.readPayDetail(spaceMemberId, payRequestId)));
    }
}
