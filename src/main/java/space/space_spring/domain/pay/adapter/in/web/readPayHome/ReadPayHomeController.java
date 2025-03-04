package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.readPayHome.ReadPayHomeUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class ReadPayHomeController {

    private final ReadPayHomeUseCase readPayHomeUseCase;

    @GetMapping("/space/{spaceId}/pay")
    public BaseResponse<ResponseOfReadPayHome> showHomeView(@JwtLoginAuth Long spaceMemberId, @PathVariable Long spaceId) {
        /**
         * 토큰 수정하면 토큰 spaceId == url spaceId 확인하는 validation 추가
         */

        return new BaseResponse<>(ResponseOfReadPayHome.of(readPayHomeUseCase.readPayHome(spaceMemberId)));
    }
}
