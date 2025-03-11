package space.space_spring.domain.post.domain;

import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;

@Getter
public class Comment {

    private Long id;

    private Long boardId;

    private Long discordId;

    private Long postId;

    private Long commentCreatorId;

    private String content;         // 댓글 내용은 String (Content 아님)

    private Boolean isAnonymous;

    private BaseInfo baseInfo;

    private Comment(Long id, Long boardId, Long discordId, Long postId, Long commentCreatorId, String content, Boolean isAnonymous, BaseInfo baseInfo) {
        this.id = id;
        this.boardId = boardId;
        this.discordId = discordId;
        this.postId = postId;
        this.commentCreatorId = commentCreatorId;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.baseInfo = baseInfo;
    }

    public static Comment create(Long id, Long boardId, Long discordId, Long postId, Long commentCreatorId, String content, Boolean isAnonymous, BaseInfo baseInfo) {
        return new Comment(id, boardId, discordId, postId, commentCreatorId, content, isAnonymous, baseInfo);
    }

    /**
     * 처음 Domain Entity 생성 시 사용하는 정적 펙토리 메서드
     */
    public static Comment withoutId(Long boardId, Long discordId, Long postId, Long commentCreatorId, String content, Boolean isAnonymous) {
        return new Comment(null, boardId, discordId, postId, commentCreatorId, content, isAnonymous, BaseInfo.ofEmpty());
    }

    public boolean isCommentCreator(Long spaceMemberId) {
        return commentCreatorId.equals(spaceMemberId);
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
