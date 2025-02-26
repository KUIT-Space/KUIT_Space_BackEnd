package space.space_spring.domain.pay.adapter.in.web.readPayRequestList;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ReadPayRequestListUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class ReadPayRequestListController {

    /**
     * 내가 요청한 정산 목록 조회
     */

    private final ReadPayRequestListUseCase readPayRequestListUseCase;

    @GetMapping("/space/{spaceId}/pay/request")
    public BaseResponse<ResponseOfReadPayRequestList> showPayRequestList(@JwtLoginAuth Long id, @PathVariable Long spaceId) {
        /**
         * 토큰 수정하면 토큰 spaceId == url spaceId 확인하는 validation 추가
         */

        return new BaseResponse<>(ResponseOfReadPayRequestList.of(readPayRequestListUseCase.readPayRequestList(id)));
    }
}
