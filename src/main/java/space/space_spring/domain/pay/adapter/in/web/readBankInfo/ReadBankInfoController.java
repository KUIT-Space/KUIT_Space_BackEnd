package space.space_spring.domain.pay.adapter.in.web.readBankInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.readBankInfo.ReadBankInfoUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class ReadBankInfoController {

    private final ReadBankInfoUseCase readBankInfoUseCase;

    @GetMapping("/space/{spaceId}/pay/bank")
    public BaseResponse<ResponseOfBankInfo> showBankInfo(@JwtLoginAuth Long spaceMemberId) {
        return new BaseResponse<>(ResponseOfBankInfo.of(readBankInfoUseCase.readBankInfo(spaceMemberId)));
    }
}
