package space.space_spring.domain.post.application.port.in.updatePost;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import space.space_spring.domain.post.domain.Attachment;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.domain.post.domain.Content;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
public class UpdatePostFromDiscordCommand {

    private Long discordId;     // 수정한 post의 discordId 값

    private String newTitle;        // 수정한 post의 title

    private Content newContent;        // 수정한 post의 content

    /**
     * key : AttachmentType, value : 해당 AttachmentType 에 해당하는 url list
     */
    @Getter(AccessLevel.NONE)
    private Map<String, AttachmentType> newAttachmentUrlMap;        // 수정한 첨부파일들

    private List<Long> discordIdOfTag;      // 수정한 post의 tag의 discordId 값들 -> 없는 경우 빈 리스트로 주십셔

    @Builder
    public UpdatePostFromDiscordCommand(Long discordId, String newTitle, Content newContent, Map<String, AttachmentType> newAttachmentUrlMap,List<Long> discordIdOfTag) {
        this.discordId = discordId;
        this.newTitle = newTitle;
        this.newContent = newContent;
        this.newAttachmentUrlMap = newAttachmentUrlMap;
    }

    public Map<AttachmentType,List<String>> getNewAttachmentUrlMap(){
        // 변환 실행
        if(newAttachmentUrlMap==null){
            return Map.of();
        }
        return newAttachmentUrlMap.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue, // AttachmentType 기준으로 그룹화
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList()) // 키 값을 리스트로 변환
                ));
    }
}
