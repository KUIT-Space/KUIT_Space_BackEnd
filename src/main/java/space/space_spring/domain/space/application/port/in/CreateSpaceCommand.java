package space.space_spring.domain.space.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter

public class CreateSpaceCommand {

    private Long guildId;

    //limit 100 char
    private String guildName;

    private Long creatorDiscordId;

    @Builder
    private CreateSpaceCommand(Long guildId,String guildName,Long creatorDiscordId){
        this.guildId=guildId;
        this.guildName = guildName;
        this.creatorDiscordId=creatorDiscordId;
    }
    static public CreateSpaceCommand of(Long guildId,String guildName,Long creatorDiscordId){
           return CreateSpaceCommand.builder()
                   .creatorDiscordId(creatorDiscordId)
                   .guildName(guildName)
                   .guildId(guildId)
                   .build();
    }
}
