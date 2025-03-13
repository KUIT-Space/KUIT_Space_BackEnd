package space.space_spring.domain.post.application.port.in.updatePost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class UpdatePostFromDiscordCommand {

    private Long discordId;     // 수정한 post의 discordId 값

    private String newTitle;        // 수정한 post의 title

    private Content newContent;        // 수정한 post의 content

    private List<String> newAttachmentUrls;        // 수정한 첨부파일의 url 들

    /**
     * 상준이가 디코에서 첨부파일들의 url 들을 줄 수 있나???
     */

    @Builder
    public UpdatePostFromDiscordCommand(Long discordId, String newTitle, Content newContent, List<String> newAttachmentUrls) {
        this.discordId = discordId;
        this.newTitle = newTitle;
        this.newContent = newContent;
        this.newAttachmentUrls = newAttachmentUrls;
    }
}
