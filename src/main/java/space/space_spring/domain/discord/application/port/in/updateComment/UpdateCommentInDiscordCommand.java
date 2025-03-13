package space.space_spring.domain.discord.application.port.in.updateComment;

import lombok.Builder;

public class UpdateCommentInDiscordCommand {

    private Long discordIdOfBoard;

    private Long discordIdOfPost;

    private Long discordIdOfComment;

    @Builder
    public UpdateCommentInDiscordCommand(Long discordIdOfBoard, Long discordIdOfPost, Long discordIdOfComment) {
        this.discordIdOfBoard = discordIdOfBoard;
        this.discordIdOfPost = discordIdOfPost;
        this.discordIdOfComment = discordIdOfComment;
    }
}
