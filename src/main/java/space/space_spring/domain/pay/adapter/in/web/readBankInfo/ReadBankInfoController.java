package space.space_spring.domain.pay.adapter.in.web.readBankInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.readBankInfo.ReadBankInfoUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pay", description = "정산 관련 API")
public class ReadBankInfoController {

    private final ReadBankInfoUseCase readBankInfoUseCase;

    @Operation(summary = "과거 정산받은 은행정보 조회", description = """
            
            스페이스 멤버가 과거에 정산받은 은행 정보를 조회힙니다.
            
            """)
    @GetMapping("/space/{spaceId}/pay/bank")
    public BaseResponse<ResponseOfBankInfo> showBankInfo(@JwtLoginAuth Long spaceMemberId, @PathVariable("spaceId") Long spaceId) {
        return new BaseResponse<>(ResponseOfBankInfo.of(readBankInfoUseCase.readBankInfo(spaceMemberId)));
    }
}
