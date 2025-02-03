package space.space_spring.domain.space.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter

public class CreateSpaceCommand {

    private Long guildId;

    //limit 100 char
    private String guildName;

    @Builder
    private CreateSpaceCommand(Long guildId,String guildName){
        this.guildId=guildId;
        this.guildName = guildName;
    }
    static public CreateSpaceCommand of(Long guildId,String guildName){
           return CreateSpaceCommand.builder()
                   .guildName(guildName)
                   .guildId(guildId)
                   .build();
    }
}
