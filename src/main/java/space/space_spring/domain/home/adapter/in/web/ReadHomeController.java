package space.space_spring.domain.home.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.home.application.port.in.ReadHomeUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Home", description = "홈 관련 API")
public class ReadHomeController {

    private final ReadHomeUseCase readHomeUseCase;

    @Operation(summary = "홈 조회", description = """

        스페이스 홈 화면을 조회합니다.

        """)
    @GetMapping("/home")
    public BaseResponse<ReadHomeResponse> readHome(@JwtLoginAuth Long spaceMemberId, @PathVariable Long spaceId) {
        return new BaseResponse<>(new ReadHomeResponse(readHomeUseCase.readHome(spaceMemberId, spaceId)));
    }
}
