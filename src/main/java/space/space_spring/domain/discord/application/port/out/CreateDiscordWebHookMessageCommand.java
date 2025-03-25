package space.space_spring.domain.discord.application.port.out;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CreateDiscordWebHookMessageCommand {

    private Long guildDiscordId;
    private Long channelDiscordId;
    private String name;
    private String avatarUrl;

    private String webHookUrl;
    private String title;
    private String content;

    private List<String> attachmentsUrl;
    @Getter(AccessLevel.NONE)
    private List<Long> tags;

    public List<Long> getTags(){
        if(tags==null){
            return List.of();
        }
        return tags;
    }
    public String getMessageContent(){
        if(content.isBlank()||content.isEmpty()){
            return getTitleAndContent();
        }

        return content+"\n\n"+getAttachmentUrlInContent();
    }

    public String getTitleAndContent(){

        return title+"\n"+content+"\n\n"+getAttachmentUrlInContent();
    }
    private String getAttachmentUrlInContent(){
        if(this.attachmentsUrl==null||this.attachmentsUrl.isEmpty()){
            return " ";
        }
        return attachmentsUrl.stream().collect(Collectors.joining("\n"));
    }

}
