package space.space_spring.domain.discord.application.port.in.updateComment;

import lombok.Builder;

public class UpdateCommentInDiscordCommand {

    private Long discordIdOfBoard;

    private Long discordIdOfPost;

    private Long discordIdOfComment;

    private String newContent;      // 수정할 댓글 내용

    @Builder
    public UpdateCommentInDiscordCommand(Long discordIdOfBoard, Long discordIdOfPost, Long discordIdOfComment, String newContent) {
        this.discordIdOfBoard = discordIdOfBoard;
        this.discordIdOfPost = discordIdOfPost;
        this.discordIdOfComment = discordIdOfComment;
        this.newContent = newContent;
    }
}
