package space.space_spring.domain.pay.adapter.in.web.readPayDetail;

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
public class ReadSinglePayController {

    /**
     * 정산 상세 조회
     */

    private final ReadPayDetailUseCase readPayDetailUseCase;

    @GetMapping("/space/{spaceId}/pay/{payRequestId}")
    public BaseResponse<ResponseOfReadPayDetail> showPayDetail(@JwtLoginAuth Long spaceMemberId, @PathVariable("spaceId") Long spaceId, @PathVariable("payRequestId") Long payRequestId) {
        /**
         * 토큰 수정하면 토큰 spaceId == url spaceId 확인하는 validation 추가
         */

        return new BaseResponse<>(ResponseOfReadPayDetail.of(readPayDetailUseCase.readPayDetail(spaceMemberId, payRequestId)));
    }
}
