package space.space_spring.domain.post.application.port.in.updatePost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.domain.post.domain.Content;

import java.util.List;
import java.util.Map;

@Getter
public class UpdatePostFromDiscordCommand {

    private Long discordId;     // 수정한 post의 discordId 값

    private String newTitle;        // 수정한 post의 title

    private Content newContent;        // 수정한 post의 content

    /**
     * key : AttachmentType, value : 해당 AttachmentType 에 해당하는 url list
     */
    private Map<AttachmentType, List<String>> newAttachmentUrlMap;        // 수정한 첨부파일들

    @Builder
    public UpdatePostFromDiscordCommand(Long discordId, String newTitle, Content newContent, Map<AttachmentType, List<String>> newAttachmentUrlMap) {
        this.discordId = discordId;
        this.newTitle = newTitle;
        this.newContent = newContent;
        this.newAttachmentUrlMap = newAttachmentUrlMap;
    }
}
