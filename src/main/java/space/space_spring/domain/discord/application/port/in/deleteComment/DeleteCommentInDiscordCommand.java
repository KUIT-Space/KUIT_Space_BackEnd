package space.space_spring.domain.discord.application.port.in.deleteComment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteCommentInDiscordCommand {

    private Long discordIdOfBoard;

    private Long discordIdOfPost;

    private Long discordIdOfComment;

    @Builder
    public DeleteCommentInDiscordCommand(Long discordIdOfBoard, Long discordIdOfPost, Long discordIdOfComment) {
        this.discordIdOfBoard = discordIdOfBoard;
        this.discordIdOfPost = discordIdOfPost;
        this.discordIdOfComment = discordIdOfComment;
    }
}
