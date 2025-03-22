package space.space_spring.domain.discord.application.port.in.updatePost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class UpdatePostInDiscordCommand {

    private Long discordIdOfBoard;     // 게시판의 디스코드 id 값

    private Long discordIdOfPost;     // 수정할 Post의 디스코드 id 값

    private String title;

    private Content content;

    private List<String> attachmentUrls;        // 새로 수정한 첨부파일의 url 들

    private List<Long> discordIdOfTags;         // 새로 수정한 tag 의 디스코드 id 값들

    private String webHookUrl;
    @Builder
    public UpdatePostInDiscordCommand(Long discordIdOfBoard, Long discordIdOfPost, String newTitle, Content newContent, List<String> newAttachmentUrls, List<Long> newDiscordIdOfTags,String webHookUrl) {
        this.discordIdOfBoard = discordIdOfBoard;
        this.discordIdOfPost = discordIdOfPost;
        this.title = newTitle;
        this.content = newContent;
        this.attachmentUrls = newAttachmentUrls;
        this.discordIdOfTags = newDiscordIdOfTags;
        this.webHookUrl=webHookUrl;
    }
}
