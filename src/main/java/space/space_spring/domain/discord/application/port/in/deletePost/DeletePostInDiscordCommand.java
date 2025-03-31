package space.space_spring.domain.discord.application.port.in.deletePost;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DeletePostInDiscordCommand {

    private Long discordIdOfBoard;

    private Long discordIdOfPost;

    private List<Long> discordIdOfComments;
    private String webHookUrl;
    private Long discordIdOfSpace;


    @Builder
    public DeletePostInDiscordCommand(String webHookUrl, Long discordIdOfSpace,Long discordIdOfPost, Long discordIdOfBoard, List<Long> discordIdOfComments) {
        this.webHookUrl=webHookUrl;
        this.discordIdOfSpace=discordIdOfSpace;
        this.discordIdOfPost = discordIdOfPost;
        this.discordIdOfBoard = discordIdOfBoard;
        this.discordIdOfComments = discordIdOfComments;
    }
}
