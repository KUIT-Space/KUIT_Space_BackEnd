package space.space_spring.domain.spaceMember.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.spaceMember.application.port.in.ReadSpaceMemberUseCase;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "SpaceMember", description = "스페이스 멤버 관련 API")
public class ReadAllSpaceMemberController {

    private final ReadSpaceMemberUseCase readSpaceMemberUseCase;

    @Operation(summary = "스페이스 전체 멤버 조회", description = """
            
            특정 스페이스에 속하는 전체 스페이스 멤버들의 정보를 조회합니다.
            
            """)
    @GetMapping("/space/{spaceId}/all-member")
    public BaseResponse<ResponseOfReadAllSpaceMember> showAllSpaceMembers(@PathVariable("spaceId") Long spaceId) {
        return new BaseResponse<>(ResponseOfReadAllSpaceMember.of(readSpaceMemberUseCase.readAllSpaceMembers(spaceId)));
    }
}
