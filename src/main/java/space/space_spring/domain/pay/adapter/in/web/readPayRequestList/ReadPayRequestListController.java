package space.space_spring.domain.pay.adapter.in.web.readPayRequestList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pay", description = "정산 관련 API")
public class ReadPayRequestListController {

    /**
     * 내가 요청한 정산 목록 조회
     */

    private final ReadPayRequestListUseCase readPayRequestListUseCase;

    @Operation(summary = "스페이스 멤버가 요청한 정산 요청 목록 조회", description = """
            
            스페이스 멤버가 요청한 모든 정산 요청들을 조회힙니다.
            
            """)
    @GetMapping("/space/{spaceId}/pay/request")
    public BaseResponse<ResponseOfReadPayRequestList> showPayRequestList(@JwtLoginAuth Long spaceMemberId) {
        return new BaseResponse<>(ResponseOfReadPayRequestList.of(readPayRequestListUseCase.readPayRequestList(spaceMemberId)));
    }
}
