package space.space_spring.domain.pay.adapter.in.web.readRequestedPayList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pay", description = "정산 관련 API")
public class ReadRequestedPayListController {

    /**
     * 요청받은 정산 목록 조회
     */

    private final ReadRequestedPayListUseCase readRequestedPayListUseCase;

    @Operation(summary = "스페이스 멤버가 요청받은 정산 요청 목록 조회", description = """
            
            스페이스 멤버가 요청받은 모든 정산 요청 목록을 조회합니다.
            
            """)
    @GetMapping("/space/{spaceId}/pay/requested")
    public BaseResponse<ResponseOfReadRequestedPayList> showRequestedPayList(@JwtLoginAuth Long spaceMemberId, @PathVariable("spaceId") Long spaceId) {
        return new BaseResponse<>(ResponseOfReadRequestedPayList.of(readRequestedPayListUseCase.readRequestedPayList(spaceMemberId)));
    }

}
