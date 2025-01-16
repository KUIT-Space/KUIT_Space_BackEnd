package space.space_spring.domain.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.userSpace.CheckUserSpace;
import space.space_spring.domain.board.model.request.CreateCommentRequest;
import space.space_spring.domain.board.model.response.ReadCommentsResponse;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.domain.board.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/board/post/{postId}/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    // 댓글 조회
    @GetMapping
    @CheckUserSpace(required = false)
    public BaseResponse<List<ReadCommentsResponse>> getComments(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId
    ) {

        // TODO 1: 특정 게시글의 댓글 리스트 get
        List<ReadCommentsResponse> comments = commentService.getCommentsByPost(postId, userId);
        return new BaseResponse<>(comments);
    }

    // 댓글 생성
    @PostMapping
    @CheckUserSpace(required = false)
    public BaseResponse<CreateCommentRequest.Response> createComment(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequest.Request createCommentRequest
            ) {

        // TODO 1: 작성한 댓글 저장 및 아이디 반환
        Long commentId = commentService.createComment(userId, postId, createCommentRequest);
        CreateCommentRequest.Response response = CreateCommentRequest.Response.of(commentId);
        return new BaseResponse<>(response);
    }

}
