package space.space_spring.domain.pay.adapter.in.web.readRequestedPayList;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ReadRequestedPayListUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class ReadRequestedPayListController {

    /**
     * 요청받은 정산 목록 조회
     */

    private final ReadRequestedPayListUseCase readRequestedPayListUseCase;

    @GetMapping("/space/{spaceId}/pay/requested")
    public BaseResponse<ResponseOfReadRequestedPayList> showRequestedPayList(@JwtLoginAuth Long spaceMemberId) {
        return new BaseResponse<>(ResponseOfReadRequestedPayList.of(readRequestedPayListUseCase.readRequestedPayList(spaceMemberId)));
    }

}
