package space.space_spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.comment.request.CreateCommentRequest;
import space.space_spring.dto.comment.response.ReadCommentsResponse;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/board/post/{postId}/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    // 댓글 조회
    @GetMapping
    public BaseResponse<List<ReadCommentsResponse>> getComments(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId
    ) {
        // TODO 1: 유저가 스페이스에 속하는 지 검증
        commentService.validateUserInSpace(userId, spaceId);

        // TODO 2: 특정 게시글의 댓글 리스트 get
        List<ReadCommentsResponse> comments = commentService.getCommentsByPost(postId, userId);
        return new BaseResponse<>(comments);
    }

    // 댓글 생성
    @PostMapping
    public BaseResponse<String> createComment(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequest createCommentRequest
            ) {
        // TODO 1: 유저가 스페이스에 속하는 지 검증
        commentService.validateUserInSpace(userId, spaceId);

        // TODO 2: 작성한 댓글 저장 및 아이디 반환
        Long commentId = commentService.createComment(userId, postId, createCommentRequest);
        return new BaseResponse<>("commentId : " + commentId);
    }

}
