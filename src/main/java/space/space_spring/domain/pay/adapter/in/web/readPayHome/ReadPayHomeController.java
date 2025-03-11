package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pay", description = "정산 관련 API")
public class ReadPayHomeController {

    private final ReadPayHomeUseCase readPayHomeUseCase;

    @Operation(summary = "정산 홈 조회", description = """
            
            스페이스 멤버가 정산 홈 화면을 조회힙니다.
            
            """)
    @GetMapping("/space/{spaceId}/pay")
    public BaseResponse<ResponseOfReadPayHome> showHomeView(@JwtLoginAuth Long spaceMemberId, @PathVariable("spaceId") Long spaceId) {
        return new BaseResponse<>(ResponseOfReadPayHome.of(readPayHomeUseCase.readPayHome(spaceMemberId)));
    }
}
