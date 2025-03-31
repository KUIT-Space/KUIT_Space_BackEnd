package space.space_spring.domain.discord.application.port.in.deleteComment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteCommentInDiscordCommand {

    private String webHookUrl;
    private Long discordIdOfBoard;

    private Long discordIdOfPost;

    private Long discordIdOfComment;
    private Long discordIdOfSpace;

    @Builder
    public DeleteCommentInDiscordCommand(String webHookUrl,Long discordIdOfSpace,Long discordIdOfBoard, Long discordIdOfPost, Long discordIdOfComment) {
        this.webHookUrl=webHookUrl;
        this.discordIdOfSpace=discordIdOfSpace;
        this.discordIdOfBoard = discordIdOfBoard;
        this.discordIdOfPost = discordIdOfPost;
        this.discordIdOfComment = discordIdOfComment;
    }
}
