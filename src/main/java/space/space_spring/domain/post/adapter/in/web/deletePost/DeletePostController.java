package space.space_spring.domain.post.adapter.in.web.deletePost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostCommand;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관련 API")
public class DeletePostController {

    private final DeletePostUseCase deletePostUseCase;

    @Operation(summary = "게시글 삭제", description = """
            
            스페이스 멤버가 자신이 생성한 게시글을 삭제합니다.
            
            """)
    @DeleteMapping("/space/{spaceId}/board/{boardId}/post/{postId}")
    public BaseResponse<SuccessResponse> deletePost(@JwtLoginAuth Long spaceMemberId,
                                                    @PathVariable Long spaceId,
                                                    @PathVariable Long boardId,
                                                    @PathVariable Long postId) {
        deletePostUseCase.deletePostFromWeb(DeletePostCommand.of(spaceId, boardId, postId, spaceMemberId));

        return new BaseResponse<>(new SuccessResponse(true));
    }


}
