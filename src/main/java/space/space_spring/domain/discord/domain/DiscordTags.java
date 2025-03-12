package space.space_spring.domain.discord.domain;

import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;

import java.util.List;

@Builder
@Getter
public class DiscordTags {
    private List<DiscordTag> tags;



    public static DiscordTags from(List<ForumTag> forumTags){

        if(forumTags==null|| forumTags.isEmpty()){
            return new DiscordTags(List.of());
        }

        return new DiscordTags(forumTags.stream().map(tag->{
            return new DiscordTag(tag.getIdLong(),tag.getName());
        }).toList());
    }

<<<<<<< Updated upstream
=======
    public static DiscordTags empty(){
        return new DiscordTags(List.of());
    }

    public boolean isEmpty(){
        if(tags==null||tags.isEmpty()){
            return true;
        }
        return false;
    }

>>>>>>> Stashed changes
    private DiscordTags(List<DiscordTag> tags){
        this.tags=tags;
    }

    private static class DiscordTag{
        @Getter
        private Long discordId;
        @Getter
        private String name;
        public DiscordTag(Long discordId, String name){
            this.discordId = discordId;
            this.name = name;
        }
    }


}
